package com.doneit.ascend.presentation.main.video_chat

import android.Manifest
import android.os.Bundle
import androidx.lifecycle.Observer
import com.androidisland.ezpermission.EzPermission
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentVideoChatBinding
import com.doneit.ascend.presentation.main.video_chat.listeners.RemoteParticipantsListener
import com.doneit.ascend.presentation.main.video_chat.listeners.RoomListener
import com.doneit.ascend.presentation.models.StartVideoModel
import com.twilio.video.*
import com.twilio.video.CameraCapturer
import kotlinx.android.synthetic.main.fragment_video_chat.*
import org.kodein.di.generic.instance


class VideoChatFragment : BaseFragment<FragmentVideoChatBinding>() {

    override val viewModelModule = VideoChatViewModelModule.get(this)
    override val viewModel: VideoChatContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        val groupId = arguments!!.getLong(GROUP_ID_ARG)
        viewModel.init(groupId)

        viewModel.credentials.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it.isMasterMind) {
                    startLocalVideo(it)
                } else {
                    startRemoteVideo(it)
                }
            }
        })
    }

    private var localVideoTrack: LocalVideoTrack? = null
    private var localAudioTrack: LocalAudioTrack? = null
    private var room: Room? = null

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
                    viewModel.onBackClick()
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

    companion object {

        private const val GROUP_ID_ARG = "GROUP_ID"

        fun newInstance(groupId: Long): VideoChatFragment {
            val fragment = VideoChatFragment()

            fragment.arguments = Bundle().apply {
                putLong(GROUP_ID_ARG, groupId)
            }

            return fragment
        }
    }
}