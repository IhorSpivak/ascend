package com.doneit.ascend.presentation.video_chat

import android.os.CountDownTimer
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.SocketEvent
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.dto.GroupCredentialsDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.group.PresentationChatParticipant
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.models.toPresentation
import com.doneit.ascend.presentation.utils.Constants.DEFAULT_MODEL_ID
import com.doneit.ascend.presentation.utils.Constants.LIST_INDEX_ABSENT
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.doneit.ascend.presentation.utils.extensions.toMinutesFormat
import com.doneit.ascend.presentation.utils.extensions.toTimerFormat
import com.doneit.ascend.presentation.video_chat.finished.ChatFinishedContract
import com.doneit.ascend.presentation.video_chat.in_progress.ChatInProgressContract
import com.doneit.ascend.presentation.video_chat.in_progress.mm_options.MMChatOptionsContract
import com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners.RoomListener
import com.doneit.ascend.presentation.video_chat.in_progress.twilio_listeners.RoomMultilistener
import com.doneit.ascend.presentation.video_chat.in_progress.user_actions.ChatParticipantActionsContract
import com.doneit.ascend.presentation.video_chat.in_progress.user_options.UserChatOptionsContract
import com.doneit.ascend.presentation.video_chat.preview.ChatPreviewContract
import com.doneit.ascend.presentation.video_chat.states.ChatRole
import com.doneit.ascend.presentation.video_chat.states.ChatStrategy
import com.doneit.ascend.presentation.video_chat.states.VideoChatState
import com.twilio.video.CameraCapturer
import com.twilio.video.RemoteParticipant
import com.twilio.video.Room
import com.twilio.video.TwilioException
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask

class VideoChatViewModel(
    private val userUseCase: UserUseCase,
    private val groupUseCase: GroupUseCase,
    private val participantsManager: ParticipantsManager
) : BaseViewModelImpl(),
    VideoChatContract.ViewModel,
    ChatPreviewContract.ViewModel,
    ChatInProgressContract.ViewModel,
    ChatFinishedContract.ViewModel,
    UserChatOptionsContract.ViewModel,
    MMChatOptionsContract.ViewModel,
    ChatParticipantActionsContract.ViewModel {

    //region data
    override val credentials = MutableLiveData<StartVideoModel>()
    override val groupInfo = MutableLiveData<GroupEntity>()
    override val participants = participantsManager.participants
    override val currentSpeaker = participantsManager.currentSpeaker
    override val navigation = SingleLiveEvent<VideoChatContract.Navigation>()
    override val roomListener = RoomMultilistener()
    //endregion

    //region ui properties
    override val timerLabel = MutableLiveData<String>()
    override val isStartButtonVisible = MutableLiveData<Boolean>()
    override val finishingLabel = MutableLiveData<String>()
    //endregion

    //region chat parameters
    private var _IsMMConnected: Boolean = false
        set(value) {
            field = value
            isMMConnected.postValue(value)
        }
    override val isMMConnected = MutableLiveData<Boolean>()
    override val isVideoEnabled = MutableLiveData<Boolean>()
    private val _isAudioEnabled = MutableLiveData<Boolean>()
    override val isAudioRecording = MediatorLiveData<Boolean>()
    override val isMuted = MutableLiveData<Boolean>()
    override val isHandRisen = MutableLiveData<Boolean>()
    override val isFinishing = MutableLiveData<Boolean>()
    override val switchCameraEvent = SingleLiveManager(Unit)
    //endregion

    //region local
    private val messages = groupUseCase.messagesStream
    private lateinit var chatState: VideoChatState
    private var chatRole: ChatRole? = null
    private var chatStrategy = ChatStrategy.DOMINANT_SPEAKER

    private var groupId: Long = DEFAULT_MODEL_ID
    private var currentUserId: String = DEFAULT_MODEL_ID.toString()
    private var downTimer: CountDownTimer? = null
    private var timer: Timer? = null
    private val hasBothCameras =
        CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.FRONT_CAMERA)
                && CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.BACK_CAMERA)
    //endregion

    init {
        isAudioRecording.addSource(_isAudioEnabled) {
            isAudioRecording.value = it && (isMuted.value?:false).not()
        }

        isAudioRecording.addSource(isMuted) {
            isAudioRecording.value = it.not() && _isAudioEnabled.value?:false
        }

        roomListener.addListener(getViewModelRoomListener())
    }

    private val messagesObserver = Observer<SocketEventEntity> { socketEvent ->
        var user = socketEvent.data.toPresentation()

        if (chatRole != null) {
            //risen hand is displayed only for MasterMind
            user = user.copy(
                isHandRisen = user.isHandRisen && chatRole == ChatRole.OWNER
            )
        }

        when (socketEvent.event) {
            SocketEvent.PARTICIPANT_CONNECTED -> {
                if (user.userId != groupInfo.value?.owner?.id.toString()
                    && user.userId != currentUserId
                ) {
                    participantsManager.addParticipant(user)
                }
            }
            SocketEvent.PARTICIPANT_DISCONNECTED -> {
                participantsManager.removeParticipant(user)
            }
            SocketEvent.RISE_A_HAND -> {
                if (currentUserId == user.userId) {
                    isHandRisen.postValue(true)
                } else {
                    participantsManager.updateHandState(user)
                }
            }
            SocketEvent.REMOVE_HAND -> {
                if (currentUserId == user.userId) {
                    isHandRisen.postValue(false)
                } else {
                    participantsManager.updateHandState(user)
                }
            }
            SocketEvent.REMOVED_FROM_GROUP -> {
                if (user.userId == currentUserId) {
                    finishCall()
                }
            }
            SocketEvent.SPEAK -> {
                if (currentUserId != user.userId) {
                    chatStrategy = ChatStrategy.USER_FOCUSED
                }

                viewModelScope.launch {
                    delay(SPEECH_FOCUS_TIME)
                    chatStrategy = ChatStrategy.DOMINANT_SPEAKER
                }

                if (chatRole == ChatRole.OWNER) {
                    groupUseCase.lowerAHand(user.userId)
                }
            }
            SocketEvent.GROUP_STARTED -> {
                groupInfo.value?.let {
                    groupUseCase.updateGroupLocal(
                        it.copy(
                            GroupStatus.STARTED
                        )
                    )
                }
                refetchGroupInfo()
                changeState(VideoChatState.PROGRESS)
            }
            SocketEvent.MUTE_USER -> {
                if(currentUserId == user.userId) {
                    isMuted.postValue(true)
                }
                participantsManager.mute(user)
            }
            SocketEvent.RESET_MUTE_USER -> {
                if(currentUserId == user.userId) {
                    isMuted.postValue(false)
                }
                participantsManager.unmute(user)
            }
        }
    }

    private fun refetchGroupInfo() {
        viewModelScope.launch {
            val res = groupUseCase.getGroupDetails(groupId)

            if (res.isSuccessful) {
                groupInfo.postValue(res.successModel!!)
            } else {
                showDefaultErrorMessage(res.errorModel!!.toErrorMessage())
            }
        }
    }

    override fun init(groupId: Long) {
        this.groupId = groupId
        postDefaultValues()

        viewModelScope.launch {

            val groupEntity = async {
                val result = groupUseCase.getGroupDetails(groupId)

                if (result.isSuccessful) {
                    groupInfo.value = result.successModel!!
                    result.successModel!!
                } else {
                    showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
                    null
                }
            }

            val userEntity = async {
                val user = userUseCase.getUser()
                currentUserId = user!!.id.toString()
                user
            }

            val creds = async {
                val result = groupUseCase.getCredentials(groupId)

                if (result.isSuccessful) {
                    result.successModel!!
                } else {
                    showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
                    null
                }
            }

            initializeChatState(groupEntity.await(), creds.await(), userEntity.await())

            loadParticipants()

            delay(PARTICIPANTS_RESYNC_DELAY)
            loadParticipants()
        }

        changeState(VideoChatState.PREVIEW)
    }

    private fun postDefaultValues() {
        isStartButtonVisible.postValue(false)
        isVideoEnabled.postValue(true)
        _isAudioEnabled.postValue(true)
        isMuted.postValue(false)
        isHandRisen.postValue(false)
        isFinishing.postValue(false)
    }

    private fun loadParticipants() {
        viewModelScope.launch {
            val result = groupUseCase.getParticipantList(groupId, isConnected = true)

            if (result.isSuccessful) {
                val participants = result.successModel!!.toMutableList()
                val currentIndex = participants.indexOfFirst { it.id.toString() == currentUserId }

                if(currentIndex != LIST_INDEX_ABSENT) {
                    isMuted.postValue(participants[currentIndex].isMuted)
                    participants.removeAt(currentIndex)
                }

                val joinedUsers = participants.filter { it.id != groupInfo.value?.owner?.id }.map {
                        var newItem = it.toPresentation()
                        chatRole?.let {
                            newItem = newItem.copy(
                                isHandRisen = newItem.isHandRisen && chatRole == ChatRole.OWNER
                            )
                        }
                        newItem
                    }

                participantsManager.addParticipants(joinedUsers)
            } else {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
        }
    }


    private fun initializeChatState(
        groupEntity: GroupEntity?,
        creds: GroupCredentialsDTO?,
        currentUser: UserEntity?
    ) {
        if (groupEntity != null && creds != null) {
            chatRole =
                if (currentUser!!.isMasterMind && groupEntity.owner!!.id == currentUser.id) {
                    ChatRole.OWNER
                } else {
                    ChatRole.VISITOR
                }

            credentials.postValue(
                StartVideoModel(
                    chatRole!!,
                    creds.name,
                    creds.token,
                    CameraCapturer.CameraSource.FRONT_CAMERA
                )
            )
            changeState(VideoChatState.PREVIEW_DATA_LOADED)
            if (chatRole == ChatRole.OWNER) {
                _IsMMConnected = true
            }
            messages.observeForever(messagesObserver)
        }
    }

    override fun onPermissionsRequired(resultCode: VideoChatActivity.ResultStatus) {
        VideoChatContract.Navigation.TO_PERMISSIONS_REQUIRED_DIALOG.data.putInt(
            ACTIVITY_RESULT_KEY,
            resultCode.ordinal
        )
        navigation.postValue(VideoChatContract.Navigation.TO_PERMISSIONS_REQUIRED_DIALOG)
    }

    override fun forceDisconnect() {
        clearChatResources()
    }

    override fun finishCall() {
        clearChatResources()
        //todo send finish message to socket
        navigation.postValue(VideoChatContract.Navigation.FINISH_ACTIVITY)
    }

    override fun onOpenOptions() {
        if (chatRole != null) {
            if (chatRole == ChatRole.OWNER) {
                navigation.postValue(VideoChatContract.Navigation.TO_MM_CHAT_OPTIONS)
            } else {
                navigation.postValue(VideoChatContract.Navigation.TO_USER_CHAT_OPTIONS)
            }
        }
    }

    override fun switchHand() {
        isHandRisen.value?.let {
            if (it) groupUseCase.lowerOwnHand() else groupUseCase.riseOwnHand()
        }
    }

    override fun switchVideoEnabledState() {
        isVideoEnabled.switch()
    }

    override fun switchAudioEnabledState() {
        _isAudioEnabled.switch()
    }

    override fun switchCamera() {
        if (hasBothCameras) {
            switchCameraEvent.call()
        }
    }

    override fun onOkClick() {
        navigation.postValue(VideoChatContract.Navigation.FINISH_ACTIVITY)
    }

    //region room events handling
    private fun getViewModelRoomListener(): RoomListener {
        return object : RoomListener() {
            override fun onConnected(room: Room) {
                val participants = room.remoteParticipants
                    .filter { it.identity != groupInfo.value?.owner?.id.toString() }
                    .map { it.toPresentation() }
                participantsManager.addParticipants(participants)
                handleSpeaker(room, room.dominantSpeaker)
            }

            override fun onParticipantConnected(room: Room, remoteParticipant: RemoteParticipant) {
                if (remoteParticipant.identity != groupInfo.value?.owner?.id?.toString()) {
                    participantsManager.addParticipant(remoteParticipant.toPresentation())
                } else {
                    _IsMMConnected = true
                }
            }

            override fun onParticipantDisconnected(
                room: Room,
                remoteParticipant: RemoteParticipant
            ) {
                participantsManager.removeParticipant(remoteParticipant.toPresentation())
                if (remoteParticipant.identity == groupInfo.value?.owner?.id?.toString()) {
                    _IsMMConnected = false
                }

                if (participantsManager.isSpeaker(remoteParticipant.identity)) {
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
        if (chatStrategy == ChatStrategy.DOMINANT_SPEAKER) {
            if (remoteParticipant != null) {
                participantsManager.onSpeakerChanged(remoteParticipant.identity)
            } else {
                displayMM(room)
            }
        }
    }

    private fun displayMM(room: Room) {
        if (canFetchMMVideo()) {
            val mmId = groupInfo.value!!.owner!!.id.toString()

            room.remoteParticipants.forEach participants@{
                if (it.identity == mmId) {
                    participantsManager.onSpeakerChanged(it.identity)
                    return@participants
                }
            }
        } else {
            participantsManager.onSpeakerChanged(null)
        }
    }

    private fun canFetchMMVideo(): Boolean {
        val isMMInfoAvailable = groupInfo.value?.owner != null
        val isRegular = chatRole == ChatRole.VISITOR

        return _IsMMConnected && isMMInfoAvailable && isRegular
    }
    //endregion

    override fun onStartGroupClick() {
        groupUseCase.startGroup()
    }

    override fun reportGroupOwner(content: String) {
        groupInfo.value?.let {
            report(content, it.owner!!.id.toString())
        }
    }

    override fun report(content: String, participantId: String) {
        viewModelScope.launch {
            val res = userUseCase.report(content, participantId)
            if (res.isSuccessful.not()) {
                showDefaultErrorMessage(res.errorModel!!.toErrorMessage())
            }
        }
    }

    override fun onNetworkStateChanged(hasConnection: Boolean) {
        if (hasConnection.not()
            && chatRole == ChatRole.OWNER
        ) {
            changeState(VideoChatState.MM_CONNECTION_LOST)
        }
    }

    override fun onParticipantClick(id: String) {
        if (chatRole == ChatRole.OWNER) {
            VideoChatContract.Navigation.TO_CHAT_PARTICIPANT_ACTIONS.data.putString(USER_ID_KEY, id)
            navigation.postValue(VideoChatContract.Navigation.TO_CHAT_PARTICIPANT_ACTIONS)
        }
    }

    override fun allowToSay(userId: String) {
        groupUseCase.allowToSay(userId)
        onBackClick()
    }

    override fun removeChatParticipant(userId: String) {
        groupUseCase.removeChatParticipant(userId)
        onBackClick()
    }

    override fun switchMuted(user: PresentationChatParticipant) {
        if(user.isMuted) {
            groupUseCase.unmuteUser(user.userId)
        } else {
            groupUseCase.muteUser(user.userId)
        }

        navigation.postValue(VideoChatContract.Navigation.BACK)
    }

    override fun onBackClick() {
        navigation.postValue(VideoChatContract.Navigation.BACK)
    }

    override fun onGoalClick() {
        VideoChatContract.Navigation.TO_GOAL.data.putLong(GROUP_ID_KEY, groupId)
        navigation.postValue(VideoChatContract.Navigation.TO_GOAL)
    }

    override fun onAttachmentsClick() {
        VideoChatContract.Navigation.TO_ATTACHMENTS.data.putLong(GROUP_ID_KEY, groupId)
        navigation.postValue(VideoChatContract.Navigation.TO_ATTACHMENTS)
    }

    override fun onNotesClick() {
        VideoChatContract.Navigation.TO_NOTES.data.putLong(GROUP_ID_KEY, groupId)
        navigation.postValue(VideoChatContract.Navigation.TO_NOTES)
    }

    override fun onCleared() {
        messages.removeObserver(messagesObserver)
        clearChatResources()
        super.onCleared()
    }

    private fun MutableLiveData<Boolean>.switch() {
        value?.let {
            postValue(!it)
        }
    }

    private fun changeState(newState: VideoChatState) {
        chatState = newState
        when (chatState) {
            VideoChatState.PREVIEW -> {
                groupUseCase.connectToChannel(groupId)
                navigation.postValue(VideoChatContract.Navigation.TO_PREVIEW)
            }
            VideoChatState.PREVIEW_DATA_LOADED -> {
                initDownTimer(groupInfo.value!!)
            }
            VideoChatState.PREVIEW_GROUP_STARTED -> {
                initProgressTimer(groupInfo.value!!)
                if (chatRole == ChatRole.OWNER) {
                    isStartButtonVisible.postValue(true)
                }
                if (groupInfo.value?.status == GroupStatus.STARTED) {
                    changeState(VideoChatState.PROGRESS)
                }
            }
            VideoChatState.MM_CONNECTION_LOST -> {
                navigation.postValue(VideoChatContract.Navigation.TO_PREVIEW)
            }
            VideoChatState.PROGRESS -> {
                navigation.postValue(VideoChatContract.Navigation.TO_CHAT_IN_PROGRESS)
            }
            VideoChatState.FINISHED -> {
                isFinishing.postValue(false)
                navigation.postValue(VideoChatContract.Navigation.TO_CHAT_FINISH)
                clearChatResources()
            }
        }
    }

    private fun initDownTimer(group: GroupEntity) {
        val currentDate = Date()
        downTimer?.cancel()
        downTimer =
            object : CountDownTimer(group.startTime!!.time - currentDate.time, TIMER_PERIOD) {
                override fun onFinish() {
                    changeState(VideoChatState.PREVIEW_GROUP_STARTED)
                }

                override fun onTick(p0: Long) {
                    timerLabel.postValue(Date(p0).toTimerFormat())
                }
            }.start()
    }

    private fun initProgressTimer(group: GroupEntity) {
        downTimer?.cancel()
        isFinishing.postValue(false)

        downTimer =
            object : CountDownTimer(
                group.timeToFinish,
                TIMER_PERIOD
            ) {
                override fun onFinish() {
                    changeState(VideoChatState.FINISHED)
                    timerLabel.postValue("00:00")
                }

                override fun onTick(p0: Long) {
                    timerLabel.postValue(Date(group.timeInProgress).toTimerFormat())
                }
            }.start()

        val finishingDate = Date(GroupEntity.FINISHING_INTERVAL + group.startTime!!.time)
        timer?.cancel()
        timer = Timer()
        timer!!.schedule(timerTask {
            isFinishing.postValue(true)
        }, finishingDate)

        timer!!.schedule(timerTask {
            finishingLabel.postValue(Date(group.timeToFinish).toMinutesFormat())
        }, finishingDate, FINISHING_TIMER_PERIOD)
    }

    private fun clearChatResources() {
        groupUseCase.disconnect()
        downTimer?.cancel()
        timer?.cancel()
    }

    companion object {
        private const val SPEECH_FOCUS_TIME = 3 * 1000L
        private const val TIMER_PERIOD = 1000L
        private const val FINISHING_TIMER_PERIOD = 1 * 60 * 1000L //every minute
        private const val PARTICIPANTS_RESYNC_DELAY = 10 * 1000L

        const val GROUP_ID_KEY = "GROUP_ID_KEY"
        const val USER_ID_KEY = "USER_ID_KEY"
        const val ACTIVITY_RESULT_KEY = "ACTIVITY_RESULT_KEY"
    }
}