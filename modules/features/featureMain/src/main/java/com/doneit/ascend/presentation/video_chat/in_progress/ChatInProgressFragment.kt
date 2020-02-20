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
import com.doneit.ascend.presentation.models.group.PresentationChatParticipant
import com.doneit.ascend.presentation.utils.extensions.requestPermissions
import com.doneit.ascend.presentation.utils.extensions.show
import com.doneit.ascend.presentation.utils.extensions.visible
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners.RemoteParticipantListener
import com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners.RoomListener
import com.twilio.video.*
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

    override val viewModel: ChatInProgressContract.ViewModel by instance()

    private var isPlaceholderAllowed = false
        set(value) {
            field = value
            placeholder?.show()//enable mm icon and group name
        }

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

    //region video chat
    private val audioCodec: AudioCodec by lazy { OpusCodec() }
    private val videoCodec: VideoCodec by lazy { Vp8Codec() }
    private val roomListener = getActivityRoomListener()
    //endregion

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        viewModel.isVideoEnabled.observe(viewLifecycleOwner, Observer {
            viewModel.localVideoTrack?.enable(it)
        })

        viewModel.isAudioRecording.observe(viewLifecycleOwner, Observer {
            viewModel.localAudioTrack?.enable(it)
        })

        viewModel.currentSpeaker.observe(viewLifecycleOwner, Observer { participant ->
            if (participant?.remoteParticipant == null) {
                showPlaceholder()
            } else {
                clearRenderers()
                participant.startVideoDisplay()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        setupAudio()
        viewModel.roomListener.addListener(roomListener)
        viewModel.credentials.observe(this, Observer {
            startVideo(it)
        })
    }

    override fun onStop() {
        viewModel.roomListener.removeListener(roomListener)
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

                if (viewModel.room == null) {

                    if (viewModel.localAudioTrack == null) {
                        viewModel.localAudioTrack =
                            LocalAudioTrack.create(activity!!.applicationContext, true)!!
                    }

                    if (viewModel.localVideoTrack == null) {
                        val capturer = CameraCapturer(
                            activity!!.applicationContext,
                            VideoChatViewModel.cameraSources.first()
                        )

                        viewModel.localVideoTrack =
                            LocalVideoTrack.create(activity!!.applicationContext, true, capturer)!!
                    }

                    val connectOptions =
                        ConnectOptions.Builder(model.accessToken)
                            .roomName(model.name)
                            .audioTracks(listOf(viewModel.localAudioTrack))
                            .preferAudioCodecs(listOf(audioCodec))
                            .videoTracks(listOf(viewModel.localVideoTrack))
                            .preferVideoCodecs(listOf(videoCodec))
                            .enableDominantSpeaker(true)
                            .build()

                    viewModel.room =
                        Video.connect(activity!!.applicationContext, connectOptions, viewModel.roomListener)
                }

                viewModel.switchCameraEvent.observe(this) {
                    (viewModel.localVideoTrack?.videoCapturer as? CameraCapturer)?.switchCamera()
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

    private fun getActivityRoomListener(): RoomListener {
        return object : RoomListener() {
            override fun onConnectFailure(room: Room, twilioException: TwilioException) {
                configureAudio(false)
            }
        }
    }

    private fun clearRenderers() {
        viewModel.room?.remoteParticipants?.forEach { roomParticipant ->
            videoView?.let {
                roomParticipant.videoTracks.firstOrNull()?.videoTrack?.removeRenderer(videoView)
            }
        }
    }

    private fun PresentationChatParticipant.startVideoDisplay() {
        setPrimaryVideoListener(getSpeakerListener())
        videoView?.let {
            val track = remoteParticipant?.videoTracks?.firstOrNull()?.videoTrack
            if (track != null) {
                track.addRenderer(videoView)
                showVideo()
            } else {
                showPlaceholder()
            }
        }
    }

    private fun getSpeakerListener(): RemoteParticipantListener {
        return object : RemoteParticipantListener() {

            override fun onVideoTrackEnabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                showVideo()
            }

            override fun onVideoTrackDisabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                showPlaceholder()
            }
        }
    }

    private fun showPlaceholder() {
        placeholder?.visible(isPlaceholderAllowed)
        videoView?.visible(false)
    }

    private fun showVideo() {
        placeholder?.visible(false)
        videoView?.visible(true)
    }
}