package com.doneit.ascend.presentation.video_chat.delegates.twilio

import androidx.fragment.app.Fragment
import com.doneit.ascend.domain.entity.group.GroupCredentialsEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.models.toPresentation
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners.RoomListener
import com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners.RoomMultilistener
import com.doneit.ascend.presentation.video_chat.states.ChatRole
import com.doneit.ascend.presentation.video_chat.states.ChatStrategy
import com.doneit.ascend.presentation.video_chat.states.VideoChatState
import com.twilio.video.*

class TwilioChatViewModelDelegate(val viewModel: VideoChatViewModel) :
    ITwilioChatViewModelDelegate {
    val roomListener = RoomMultilistener()
    val cameraSources: List<CameraCapturer.CameraSource>

    var room: Room? = null
        set(value) {
            field?.disconnect()
            field = value
        }
    var localAudioTrack: LocalAudioTrack? = null
        set(value) {
            field?.release()
            field = value
        }
    var localVideoTrack: LocalVideoTrack? = null
        set(value) {
            field?.release()
            field = value
        }

    init {
        val sources = mutableListOf<CameraCapturer.CameraSource>()
        if (CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.FRONT_CAMERA)) {
            sources.add(CameraCapturer.CameraSource.FRONT_CAMERA)
        }
        if (CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.BACK_CAMERA)) {
            sources.add(CameraCapturer.CameraSource.BACK_CAMERA)
        }

        cameraSources = sources

        roomListener.addListener(getViewModelRoomListener())
    }

    override fun initializeChatState(
        groupEntity: GroupEntity?,
        creds: GroupCredentialsEntity?,
        currentUser: UserEntity?
    ) {
        viewModel.chatRole =
            if (currentUser!!.isMasterMind && groupEntity!!.owner!!.id == currentUser.id) {
                ChatRole.OWNER
            } else {
                ChatRole.VISITOR
            }

        viewModel.credentials.postValue(
            StartVideoModel.TwilioVideoModel(
                viewModel.chatRole!!,
                creds!!.name,
                creds.token
            )
        )
        viewModel.changeState(VideoChatState.PREVIEW_DATA_LOADED)
        if (viewModel.chatRole == ChatRole.OWNER) {
            viewModel._IsMMConnected = true
        }

        viewModel.messages.observeForever(viewModel.messagesObserver)
    }

    override fun getCameraCount(): Int {
        return cameraSources.size
    }

    //region room events handling

    private fun getViewModelRoomListener(): RoomListener {
        return object : RoomListener() {
            override fun onConnected(room: Room) {
                val participants = room.remoteParticipants
                    .map { it.toPresentation() }.toMutableList()
                room.localParticipant?.let { if(it.identity == viewModel.groupInfo.value?.owner?.id.toString()) participants.add(it.toPresentation()) }
                viewModel.participantsManager.addParticipants(participants)
                handleSpeaker(room, room.dominantSpeaker)
            }



            override fun onParticipantConnected(room: Room, remoteParticipant: RemoteParticipant) {
                viewModel.participantsManager.addParticipant(remoteParticipant.toPresentation())
                if (remoteParticipant.identity == viewModel.groupInfo.value?.owner?.id?.toString()) {
                    viewModel._IsMMConnected = true
                }
            }

            override fun onParticipantDisconnected(
                room: Room,
                remoteParticipant: RemoteParticipant
            ) {
                viewModel.participantsManager.removeParticipant(remoteParticipant.toPresentation())
                if (remoteParticipant.identity == viewModel.groupInfo.value?.owner?.id?.toString()) {
                    viewModel._IsMMConnected = false
                }

                if (viewModel.participantsManager.isSpeaker(remoteParticipant.identity)) {
                    handleSpeaker(room, room.dominantSpeaker)
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

    private fun handleSpeaker(room: Room, remoteParticipant: RemoteParticipant?) {
        if (viewModel.chatStrategy == ChatStrategy.DOMINANT_SPEAKER) {
            if (remoteParticipant != null) {
                viewModel.participantsManager.onSpeakerChanged(remoteParticipant.identity)
            } else {
                displayMM(room)
            }
        }
    }

    private fun displayMM(room: Room) {
        if (canFetchMMVideo()) {
            val mmId = viewModel.groupInfo.value!!.owner!!.id.toString()

            room.remoteParticipants.forEach participants@{
                if (it.identity == mmId) {
                    viewModel.participantsManager.onSpeakerChanged(it.identity)
                    return@participants
                }
            }
        } else {
            viewModel.participantsManager.onSpeakerChanged(null)
        }
    }

    private fun canFetchMMVideo(): Boolean {
        val isMMInfoAvailable = viewModel.groupInfo.value?.owner != null
        val isRegular = viewModel.chatRole == ChatRole.VISITOR

        return viewModel._IsMMConnected && isMMInfoAvailable && isRegular
    }

    //endregion

    override fun clearChatResources() {
        localAudioTrack = null
        localVideoTrack = null
        room = null
    }

    fun startVideo(
        fragment: Fragment,
        model: StartVideoModel.TwilioVideoModel,
        audioCodec: AudioCodec,
        videoCodec: VideoCodec
    ) {
        val context = fragment.activity?.applicationContext ?: return
        if (room == null) {

            if (localAudioTrack == null) {
                localAudioTrack =
                    LocalAudioTrack.create(context, true)!!
            }

            if (localVideoTrack == null) {
                val capturer = CameraCapturer(
                    context,
                    cameraSources.first()
                )

                localVideoTrack =
                    LocalVideoTrack.create(context, true, capturer)!!
            }

            val connectOptions =
                ConnectOptions.Builder(model.accessToken)
                    .roomName(model.name)
                    .audioTracks(listOf(localAudioTrack))
                    .preferAudioCodecs(listOf(audioCodec))
                    .videoTracks(listOf(localVideoTrack))
                    .preferVideoCodecs(listOf(videoCodec))
                    .enableDominantSpeaker(true)
                    .build()

            room =
                Video.connect(
                    context,
                    connectOptions,
                    roomListener
                )
        }
    }

    fun startSelfView(fragment: Fragment) {

    }

    fun switchCamera() {
        (localVideoTrack?.videoCapturer as? CameraCapturer)?.switchCamera()
    }
}