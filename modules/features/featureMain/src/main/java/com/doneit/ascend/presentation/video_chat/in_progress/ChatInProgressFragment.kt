package com.doneit.ascend.presentation.video_chat.in_progress

import android.Manifest
import android.content.Context
import android.content.IntentFilter
import android.media.AudioManager
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentVideoChatBinding
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.utils.extensions.*
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import com.doneit.ascend.presentation.video_chat.in_progress.listeners.RemoteParticipantsListener
import com.doneit.ascend.presentation.video_chat.in_progress.listeners.RoomListener
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
    private val brHeadphones = HeadphonesBroadcastReceiver {
        isSpeakerPhoneEnabled = it
    }
    //endregion

    //region video chat
    private val audioCodec: AudioCodec by lazy { OpusCodec() }
    private val videoCodec: VideoCodec by lazy { Vp8Codec() }//todo: check H264
    private var localVideoTrack: LocalVideoTrack? = null
    private var localAudioTrack: LocalAudioTrack? = null
    private var room: Room? = null
    //endregion

    private var chatStrategy = ChatStrategy.DOMINANT_SPEAKER

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        viewModel.isVideoEnabled.observe(viewLifecycleOwner, Observer {
            localVideoTrack?.enable(it)
            //binding.placeholder.visible(it.not())
        })

        viewModel.isAudioEnabled.observe(viewLifecycleOwner, Observer {
            localAudioTrack?.enable(it)
        })

        viewModel.isRecordEnabled.observe(viewLifecycleOwner, Observer {
            //todo
        })

        viewModel.focusedUserId.observe(viewLifecycleOwner, Observer { id ->
            if(id == VideoChatViewModel.UNFOCUSED_USER_ID) {
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

    override fun onStart() {
        super.onStart()
        viewModel.credentials.observe(this, Observer {
            startVideo(it)
            binding.grPlaceholderData.show()//enable mm icon and group name
        })
    }

    private fun startVideo(model: StartVideoModel) {
        context!!.requestPermissions(
            listOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            ),
            onGranted = {
                val cameraCapturer = CameraCapturer(
                    context!!,
                    model.camera
                )

                localAudioTrack = LocalAudioTrack.create(context!!, true)
                localVideoTrack = LocalVideoTrack.create(context!!, true, cameraCapturer)!!
                localVideoTrack?.addRenderer(videoView)
                binding.placeholder.visible(false)

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

    private fun getUserRoomListener(): RoomListener {
        return object : RoomListener() {

            override fun onConnected(room: Room) {
                room.displayDominant()
            }

            override fun onParticipantDisconnected(
                room: Room,
                remoteParticipant: RemoteParticipant
            ) {
                val track = remoteParticipant.videoTracks.firstOrNull()?.videoTrack
                val renderersCount = track?.renderers?.size?: 0
                if(renderersCount > 0) {
                    track?.removeRenderer(videoView)
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
        if(chatStrategy == ChatStrategy.DOMINANT_SPEAKER) {
            handleSpeaker(this, this.dominantSpeaker)
        }
    }

    private fun handleSpeaker(room: Room, remoteParticipant: RemoteParticipant?) {
        localVideoTrack?.removeRenderer(videoView)
        room.remoteParticipants.forEach {
            it.clearRenderer()
        }

        if (remoteParticipant != null) {
            remoteParticipant.setListener(getParticipantsListener())
            remoteParticipant.startVideoDisplay()
            binding.placeholder.visible(false)
        } else {
            localVideoTrack?.addRenderer(videoView)
        }
    }

    private fun getParticipantsListener(): RemoteParticipantsListener {
        return object : RemoteParticipantsListener() {

            override fun onVideoTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                remoteVideoTrack: RemoteVideoTrack
            ) {
                //todo how to unsubscribe at appropriate time?
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                    binding.placeholder.show()
                }
            }

            override fun onVideoTrackEnabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                binding.placeholder.visible(false)
            }

            override fun onVideoTrackDisabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                binding.placeholder.show()
            }
        }
    }

    private fun Participant.clearRenderer() {
        videoTracks.firstOrNull()?.videoTrack?.removeRenderer(videoView)
    }

    private fun Participant.startVideoDisplay() {
        videoTracks.firstOrNull()?.videoTrack?.addRenderer(videoView)
    }

    override fun onStop() {
        localAudioTrack?.release()
        localVideoTrack?.release()
        room?.disconnect()
        viewModel.forceDisconnect()
        super.onStop()
    }

    override fun onDestroyView() {
        requireContext().unregisterReceiver(brHeadphones)
        super.onDestroyView()
    }

    private fun Room.getParticipantById(id: String): RemoteParticipant? {
        return remoteParticipants.find { it.identity == id }
    }
}