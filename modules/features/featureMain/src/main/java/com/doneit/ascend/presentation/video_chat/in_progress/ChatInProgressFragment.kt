package com.doneit.ascend.presentation.video_chat.in_progress

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.hardware.Camera
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
import com.doneit.ascend.presentation.utils.extensions.show
import com.doneit.ascend.presentation.utils.extensions.visible
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners.RemoteParticipantsListener
import com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners.RoomListener
import com.doneit.ascend.presentation.video_chat.states.ChatStrategy
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

    //region audio
    private val audioManager by lazy {
        activity?.getSystemService(Context.AUDIO_SERVICE) as AudioManager
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
    private var localVideoTrack: LocalVideoTrack? = null
    private var localAudioTrack: LocalAudioTrack? = null
    private var room: Room? = null
    //endregion

    private var chatStrategy = ChatStrategy.DOMINANT_SPEAKER
    private var isPlaceholderAllowed = false
        set(value) {
            field = value
            placeholder?.show()//enable mm icon and group name
        }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        viewModel.isVideoEnabled.observe(viewLifecycleOwner, Observer {
            localVideoTrack?.enable(it)
        })

        viewModel.isAudioEnabled.observe(viewLifecycleOwner, Observer {
            localAudioTrack?.enable(it)
        })

        viewModel.focusedUserId.observe(viewLifecycleOwner, Observer { id ->
            if (id == VideoChatViewModel.UNFOCUSED_USER_ID) {
                chatStrategy = ChatStrategy.DOMINANT_SPEAKER
                room?.let {
                    handleSpeaker(room!!, room!!.dominantSpeaker)
                }
            } else {
                chatStrategy = ChatStrategy.USER_FOCUSED
                room?.let {
                    handleSpeaker(room!!, room!!.getParticipantById(id))
                }
            }
        })

        setupAudio()
    }

    private fun setupAudio() {
        requireContext().registerReceiver(
            brHeadphones,
            IntentFilter(AudioManager.ACTION_HEADSET_PLUG)
        )
        activity?.volumeControlStream = AudioManager.STREAM_VOICE_CALL
        audioManager.isSpeakerphoneOn = isSpeakerPhoneEnabled
    }

    private fun Room.getParticipantById(id: String): RemoteParticipant? {
        return remoteParticipants.find { it.identity == id }
    }

    override fun onStart() {
        super.onStart()
        viewModel.credentials.observe(this, Observer {
            startVideo(it)
        })
    }

    private fun startVideo(model: StartVideoModel) {
        context!!.requestPermissions(
            listOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            ),
            onGranted = {
                configureAudio(true)
                val cameraCapturer = CameraCapturer(
                    context!!,
                    model.camera
                )

                cameraCapturer.updateCameraParameters(object : CameraParameterUpdater {
                    override fun apply(cameraParameters: Camera.Parameters) {

                    }
                })

                localAudioTrack = LocalAudioTrack.create(context!!, true)
                localVideoTrack = LocalVideoTrack.create(context!!, true, cameraCapturer)!!

                val connectOptions =
                    ConnectOptions.Builder(model.accessToken)
                        .roomName(model.name)
                        .audioTracks(listOf(localAudioTrack))
                        .preferAudioCodecs(listOf(audioCodec))
                        .videoTracks(listOf(localVideoTrack))
                        .preferVideoCodecs(listOf(videoCodec))
                        .enableDominantSpeaker(true)
                        .build()

                room = Video.connect(context!!, connectOptions, getUserRoomListener())

                viewModel.switchCameraEvent.observe(viewLifecycleOwner) {
                    cameraCapturer.switchCamera()
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

    private fun getUserRoomListener(): RoomListener {
        return object : RoomListener() {

            override fun onConnected(room: Room) {
                viewModel.onConnected(room.remoteParticipants.map { it.identity })
                if (room.remoteParticipants.size > 0) {
                    isPlaceholderAllowed = true
                }
                room.displayDominant()
            }

            override fun onConnectFailure(room: Room, twilioException: TwilioException) {
                configureAudio(false)
            }

            override fun onParticipantConnected(room: Room, remoteParticipant: RemoteParticipant) {
                viewModel.onUserConnected(remoteParticipant.identity)
                isPlaceholderAllowed = true
            }

            override fun onParticipantDisconnected(
                room: Room,
                remoteParticipant: RemoteParticipant
            ) {
                viewModel.onUserDisconnected(remoteParticipant.identity)

                if (viewModel.isSpeaker(remoteParticipant.identity)) {
                    room.displayDominant()
                }
            }

            override fun onDominantSpeakerChanged(
                room: Room,
                remoteParticipant: RemoteParticipant?
            ) {
                handleSpeaker(room, remoteParticipant)
            }
        }
    }

    private fun Room.displayDominant() {
        if (chatStrategy == ChatStrategy.DOMINANT_SPEAKER) {
            handleSpeaker(this, this.dominantSpeaker)
        }
    }

    private fun handleSpeaker(room: Room, remoteParticipant: RemoteParticipant?) {
        room.remoteParticipants.forEach {
            it.clearRenderer()
        }

        if (remoteParticipant != null) {
            remoteParticipant.startVideoDisplay()
        } else {
            room.displayMM()
        }
    }

    private fun Room.displayMM() {
        if (viewModel.canFetchMMVideo()) {
            val mmId = viewModel.groupInfo.value!!.owner!!.id.toString()

            remoteParticipants.forEach participants@{
                if (it.identity == mmId) {
                    it.startVideoDisplay()
                    return@participants
                }
            }
        } else {
            viewModel.onSpeakerChanged(null)
            showPlaceholder()
        }
    }

    private fun Participant.clearRenderer() {
        videoView?.let {
            videoTracks.firstOrNull()?.videoTrack?.removeRenderer(videoView)
        }
    }

    private fun RemoteParticipant.startVideoDisplay() {
        if (viewModel.isSpeaker(identity).not()) {
            viewModel.onSpeakerChanged(identity)
            setListener(getParticipantsListener())
            videoView?.let {
                videoTracks.firstOrNull()?.videoTrack?.addRenderer(videoView)
            }
            showVideo()
        }
    }

    private fun getParticipantsListener(): RemoteParticipantsListener {
        return object : RemoteParticipantsListener() {

            override fun onVideoTrackEnabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                if (viewModel.isSpeaker(remoteParticipant.identity)) {
                    showVideo()
                }
            }

            override fun onVideoTrackDisabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                if (viewModel.isSpeaker(remoteParticipant.identity)) {
                    showPlaceholder()
                }
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

    override fun onStop() {
        localAudioTrack?.release()
        localAudioTrack = null
        localVideoTrack?.release()
        localVideoTrack = null
        room?.disconnect()
        room = null
        configureAudio(false)
        viewModel.forceDisconnect()
        super.onStop()
    }

    override fun onDestroyView() {
        requireContext().unregisterReceiver(brHeadphones)
        super.onDestroyView()
    }
}