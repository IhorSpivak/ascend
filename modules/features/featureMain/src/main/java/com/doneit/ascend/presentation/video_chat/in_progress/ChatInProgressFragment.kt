package com.doneit.ascend.presentation.video_chat.in_progress

import android.Manifest
import android.os.Bundle
import androidx.lifecycle.Observer
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentVideoChatBinding
import com.doneit.ascend.presentation.main.extensions.vmShared
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import com.doneit.ascend.presentation.video_chat.listeners.RemoteParticipantsListener
import com.doneit.ascend.presentation.video_chat.listeners.RoomListener
import com.doneit.ascend.presentation.models.StartVideoModel
import com.twilio.video.*
import kotlinx.android.synthetic.main.fragment_video_chat.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class ChatInProgressFragment : BaseFragment<FragmentVideoChatBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ChatInProgressContract.ViewModel>() with provider { vmShared<VideoChatViewModel>(instance()) }
    }
    override val viewModel: ChatInProgressContract.ViewModel by instance()

    private var localVideoTrack: LocalVideoTrack? = null
    private var localAudioTrack: LocalAudioTrack? = null
    private var room: Room? = null

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        viewModel.credentials.observe(this, Observer {
            it?.let {
                if (it.isMasterMind) {
                    startLocalVideo(it)
                } else {
                    startRemoteVideo(it)
                }
            }
        })
    }

    private fun startLocalVideo(model: StartVideoModel) {
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
                        CameraCapturer.CameraSource.FRONT_CAMERA
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
                    if (it.remoteVideoTracks.isNotEmpty()) {
                        it.setListener(getParticipantsListener())
                        return
                    }
                }
            }

            override fun onParticipantConnected(room: Room, remoteParticipant: RemoteParticipant) {
                if (remoteParticipant.remoteVideoTracks.isNotEmpty()) {
                    remoteParticipant.setListener(getParticipantsListener())
                    return
                }
            }
        }
    }

    private fun getParticipantsListener(): RemoteParticipantsListener {
        return object : RemoteParticipantsListener() {
            override fun onVideoTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                remoteVideoTrack: RemoteVideoTrack
            ) {
                remoteVideoTrack.addRenderer(videoView)
            }
        }
    }

    override fun onDestroyView() {
        room?.disconnect()
        localAudioTrack?.release()
        localVideoTrack?.release()
        super.onDestroyView()
    }

}