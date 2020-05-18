package com.doneit.ascend.presentation.video_chat.in_progress

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentVideoChatBinding
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.utils.extensions.requestPermissions
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import com.doneit.ascend.presentation.video_chat.delegates.VideoChatViewDelegate
import com.doneit.ascend.presentation.video_chat.delegates.twilio.TwilioChatViewDelegate
import com.doneit.ascend.presentation.video_chat.delegates.twilio.TwilioChatViewModelDelegate
import com.doneit.ascend.presentation.video_chat.delegates.vimeo.VimeoChatViewDelegate
import com.doneit.ascend.presentation.video_chat.delegates.vimeo.VimeoChatViewModelDelegate
import kotlinx.android.synthetic.main.fragment_video_chat.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider


class ChatInProgressFragment : BaseFragment<FragmentVideoChatBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ChatInProgressContract.ViewModel>() with provider {
            vmShared<VideoChatViewModel>(
                instance()
            )
        }
    }

    override val viewModel by instance<ChatInProgressContract.ViewModel>()

    private var delegate: VideoChatViewDelegate? = null

    //region audio
    private val audioManager by lazy {
        activity!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private var isSpeakerPhoneEnabled = true
        set(value) {
            field = value
            audioManager.isSpeakerphoneOn = field
        }
    private var previousAudioMode = 0
    private var previousMicrophoneMute = false
    private val brHeadphones = HeadphonesBroadcastReceiver {
        isSpeakerPhoneEnabled = it
    }
    //endregion

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        delegate = when (viewModel.viewModelDelegate) {
            is TwilioChatViewModelDelegate -> TwilioChatViewDelegate(
                this,
                videoView,
                placeholder,
                viewModel.viewModelDelegate as TwilioChatViewModelDelegate
            ) { configureAudio(it) }
            is VimeoChatViewModelDelegate -> VimeoChatViewDelegate(viewModel.viewModelDelegate as VimeoChatViewModelDelegate)
            else -> null
        }

        viewModel.isVideoEnabled.observe(viewLifecycleOwner, Observer {
            delegate?.enableVideo(it)
        })

        viewModel.isAudioRecording.observe(viewLifecycleOwner, Observer {
            delegate?.enableAudio(it)
        })

        viewModel.currentSpeaker.observe(viewLifecycleOwner, Observer { participant ->
            if (participant?.remoteParticipant == null) {
                delegate?.showPlaceholder()
            } else {
                clearRenderers()
                delegate?.startVideoDisplay(participant)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        setupAudio()
        delegate?.onStart()
        viewModel.credentials.observe(this, Observer {
            startVideo(it)
        })
    }

    override fun onStop() {
        delegate?.onStop()
        configureAudio(false)
        activity!!.unregisterReceiver(brHeadphones)
        super.onStop()
    }

    private fun setupAudio() {
        requireContext().registerReceiver(
            brHeadphones,
            IntentFilter(AudioManager.ACTION_HEADSET_PLUG)
        )
        activity!!.volumeControlStream = AudioManager.STREAM_VOICE_CALL
        audioManager.isSpeakerphoneOn = isSpeakerPhoneEnabled
    }

    private fun startVideo(model: StartVideoModel) {
        context!!.requestPermissions(
            listOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            ),
            onGranted = {
                delegate?.startVideo(model)
                viewModel.switchCameraEvent.observe(this) {
                    delegate?.switchCamera()
                }
            },
            onDenied = {
                viewModel.onPermissionsRequired(VideoChatActivity.ResultStatus.POPUP_REQUIRED)
            }
        )
    }

    private fun configureAudio(enable: Boolean) {
        with(audioManager) {
            if (enable) {
                previousAudioMode = audioManager.mode
                requestAudioFocus()

                mode = AudioManager.MODE_IN_COMMUNICATION
                /*
                 * Always disable microphone mute during a WebRTC call.
                 */
                previousMicrophoneMute = isMicrophoneMute
                isMicrophoneMute = false
            } else {
                mode = previousAudioMode
                abandonAudioFocus(null)
                isMicrophoneMute = previousMicrophoneMute
            }
        }
    }

    private fun requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val playbackAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                .setAudioAttributes(playbackAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener { }
                .build()
            audioManager.requestAudioFocus(focusRequest)
        } else {
            audioManager.requestAudioFocus(
                null, AudioManager.STREAM_VOICE_CALL,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )
        }
    }

    private fun clearRenderers() {
        delegate?.clearRenderers()
    }
}