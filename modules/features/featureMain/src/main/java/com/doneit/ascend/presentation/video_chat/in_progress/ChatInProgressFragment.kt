package com.doneit.ascend.presentation.video_chat.in_progress

import android.Manifest
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

    private var localVideoTrack: LocalVideoTrack? = null
    private var localAudioTrack: LocalAudioTrack? = null
    private var room: Room? = null

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
                binding.placeholder.hide()


                val connectOptions =
                    ConnectOptions.Builder(model.accessToken)
                        .roomName(model.name)
                        .audioTracks(listOf(localAudioTrack))
                        .videoTracks(listOf(localVideoTrack))
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
                handleDominantSpeaker(room, room.dominantSpeaker)
            }

            override fun onParticipantDisconnected(
                room: Room,
                remoteParticipant: RemoteParticipant
            ) {
                if (viewModel.onUserDisconnected(remoteParticipant.identity)) {
                    remoteParticipant.clearRenderer()
                    placeholder.show()
                }
            }

            override fun onDominantSpeakerChanged(
                room: Room,
                remoteParticipant: RemoteParticipant?
            ) {
                handleDominantSpeaker(room, remoteParticipant)
            }
        }
    }

    private fun handleDominantSpeaker(room: Room, remoteParticipant: RemoteParticipant?) {
        remoteParticipant?.let {
            room.remoteParticipants.forEach {
                it.clearRenderer()
            }

            viewModel.onSpeakerChanged(remoteParticipant.identity)
            remoteParticipant.setListener(getParticipantsListener())
            remoteParticipant.startVideoDisplay()
            binding.placeholder.hide()
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
                binding.placeholder.hide()
            }

            override fun onVideoTrackDisabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                binding.placeholder.show()
            }
        }
    }

    private fun RemoteParticipant.clearRenderer() {
        videoTracks.firstOrNull()?.videoTrack?.removeRenderer(videoView)
    }

    private fun RemoteParticipant.startVideoDisplay() {
        videoTracks.firstOrNull()?.videoTrack?.addRenderer(videoView)
    }

    override fun onStop() {
        localAudioTrack?.release()
        localVideoTrack?.release()
        room?.disconnect()
        viewModel.forceDisconnect()
        super.onStop()
    }
}