package com.doneit.ascend.presentation.video_chat_webinar.in_progress

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentVideoChatWebinarBinding
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat.delegates.VideoChatUtils
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatViewModel
import com.doneit.ascend.presentation.video_chat_webinar.delegate.vimeo.VimeoChatViewDelegate
import kotlinx.android.synthetic.main.fragment_video_chat.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class WebinarVideoChatInProgressFragment : BaseFragment<FragmentVideoChatWebinarBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<WebinarVideoChatInProgressContract.ViewModel>() with provider {
            vmShared<WebinarVideoChatViewModel>(
                instance()
            )
        }
    }

    override val viewModel: WebinarVideoChatInProgressContract.ViewModel by instance()

    private var delegate: VimeoChatViewDelegate? = null

    private val audioManager by lazy {
        activity!!.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }
    private var previousAudioMode = 0
    private var previousMicrophoneMute = false

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.send.setOnClickListener {
            if (binding.message.text.isNotBlank()) {
                viewModel.createQuestion(binding.message.text.trim().toString())
                binding.message.text.clear()
            }
        }
        delegate = VideoChatUtils.newVimeoViewModelDelegate(viewModel.viewModelDelegate, placeholder) {
            fragment = this@WebinarVideoChatInProgressFragment
        }
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
}