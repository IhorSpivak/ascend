package com.doneit.ascend.presentation.video_chat.in_progress

import android.Manifest
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentVideoChatBinding
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.utils.extensions.hide
import com.doneit.ascend.presentation.utils.extensions.show
import com.doneit.ascend.presentation.utils.extensions.visible
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import com.doneit.ascend.presentation.video_chat.listeners.RemoteParticipantsListener
import com.doneit.ascend.presentation.video_chat.listeners.RoomListener
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
        binding.isMMConnected = true

        viewModel.credentials.observe(this, Observer {
            it?.let {
                if (it.isTranslator) {
                    startLocalVideo(it)
                } else {
                    startRemoteVideo(it)
                }
            }
        })

        viewModel.isVideoEnabled.observe(viewLifecycleOwner, Observer {
            localVideoTrack?.enable(it)
            binding.placeholder.visible(it.not())
        })

        viewModel.isAudioEnabled.observe(viewLifecycleOwner, Observer {
            localAudioTrack?.enable(it)
        })

        viewModel.isRecordEnabled.observe(viewLifecycleOwner, Observer {
            //todo
        })
    }

    private fun startLocalVideo(model: StartVideoModel) {
        binding.placeholder.hide()
        EzPermission.with(context!!)
            .permissions(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            )
            .request { granted, _, _ ->
                if (granted.containsAll(
                        listOf(
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.CAMERA
                        )
                    )
                ) {

                    val cameraCapturer = CameraCapturer(
                        context!!,
                        model.camera
                    )

                    localAudioTrack = LocalAudioTrack.create(context!!, true)
                    localVideoTrack = LocalVideoTrack.create(context!!, true, cameraCapturer)!!
                    localVideoTrack!!.addRenderer(videoView)

                    val connectOptions =
                        ConnectOptions.Builder(model.accessToken)
                            .roomName(model.name)
                            .audioTracks(listOf(localAudioTrack))
                            .videoTracks(listOf(localVideoTrack))
                            .build()

                    room = Video.connect(context!!, connectOptions, RoomListener())
                } else {
                    viewModel.onPermissionsRequired(VideoChatActivity.ResultStatus.POPUP_REQUIRED)
                }
            }
    }

    private fun startRemoteVideo(model: StartVideoModel) {
        val connectOptions =
            ConnectOptions.Builder(model.accessToken)
                .roomName(model.name)
                .build()

        room = Video.connect(context!!, connectOptions, getRoomListener())
    }

    private fun getRoomListener(): RoomListener {
        return object : RoomListener() {
            override fun onConnected(room: Room) {
                room.remoteParticipants.forEach {
                    if (checkForVideoStream(it)) {
                        binding.isMMConnected = true
                        return
                    }
                }
            }

            override fun onParticipantConnected(room: Room, remoteParticipant: RemoteParticipant) {
                if(checkForVideoStream(remoteParticipant)) {
                    binding.isMMConnected = true
                }
            }


            override fun onParticipantDisconnected(
                room: Room,
                remoteParticipant: RemoteParticipant
            ) {
                if(viewModel.isSubscribedTo(remoteParticipant.identity)){
                    binding.isMMConnected = false
                    placeholder.show()
                }
            }
        }
    }

    private fun checkForVideoStream(participant: RemoteParticipant): Boolean {
        if (participant.remoteVideoTracks.isNotEmpty()) {
            viewModel.onVideoStreamSubscribed(participant.identity)
            participant.setListener(getParticipantsListener())
            return true
        }
        return false
    }

    private fun getParticipantsListener(): RemoteParticipantsListener {
        return object : RemoteParticipantsListener() {

            override fun onVideoTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                remoteVideoTrack: RemoteVideoTrack
            ) {
                remoteVideoTrackPublication.remoteVideoTrack?.startVideoDisplay()
            }

            override fun onVideoTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                remoteVideoTrack: RemoteVideoTrack
            ) {
                //todo how to unsubscribe at appropriate time?
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)){
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

    private fun RemoteVideoTrack.startVideoDisplay() {
        this.addRenderer(videoView)
        binding.placeholder.hide()
    }

    override fun onDestroyView() {
        localAudioTrack?.release()
        localVideoTrack?.release()
        room?.disconnect()
        viewModel.forceDisconnect()
        super.onDestroyView()
    }
}