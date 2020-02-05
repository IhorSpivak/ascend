package com.doneit.ascend.presentation.video_chat

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.SocketEvent
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.dto.GroupCredentialsModel
import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.PresentationChatParticipant
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.models.toPresentation
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.doneit.ascend.presentation.utils.extensions.toMinutesFormat
import com.doneit.ascend.presentation.utils.extensions.toTimerFormat
import com.doneit.ascend.presentation.video_chat.finished.ChatFinishedContract
import com.doneit.ascend.presentation.video_chat.in_progress.ChatInProgressContract
import com.doneit.ascend.presentation.video_chat.in_progress.mm_options.MMChatOptionsContract
import com.doneit.ascend.presentation.video_chat.in_progress.user_actions.ChatParticipantActionsContract
import com.doneit.ascend.presentation.video_chat.in_progress.user_options.UserChatOptionsContract
import com.doneit.ascend.presentation.video_chat.preview.ChatPreviewContract
import com.doneit.ascend.presentation.video_chat.states.ChatRole
import com.doneit.ascend.presentation.video_chat.states.VideoChatState
import com.twilio.video.CameraCapturer
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask

class VideoChatViewModel(
    private val router: VideoChatContract.Router,
    private val userUseCase: UserUseCase,
    private val groupUseCase: GroupUseCase
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
    override val participants = MutableLiveData<List<PresentationChatParticipant>>(listOf())
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
    override val isAudioEnabled = MutableLiveData<Boolean>()
    override val isHandRisen = MutableLiveData<Boolean>()
    override val isFinishing = MutableLiveData<Boolean>()
    override val switchCameraEvent = SingleLiveManager(Unit)
    override val focusedUserId = SingleLiveEvent<String>()
    //endregion

    //region local
    private val messages = groupUseCase.messagesStream
    private lateinit var chatState: VideoChatState
    private var chatRole: ChatRole? = null

    private var groupId: Long = -1
    private var currentUserId: Long = -1
    private var currentSpeakerId: String? = null
    private var downTimer: CountDownTimer? = null
    private var timer: Timer? = null
    private val hasBothCameras =
        CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.FRONT_CAMERA)
                && CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.BACK_CAMERA)
    //endregion

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
                if (user.userId != groupInfo.value?.owner?.id
                    && user.userId != currentUserId
                ) {
                    val newList = participants.value!!.toMutableList()
                    if (newList.find { it.userId == user.userId } == null) {
                        newList.add(user)
                        participants.postValue(newList)
                    }
                }
            }
            SocketEvent.PARTICIPANT_DISCONNECTED -> {
                val newList = participants.value!!.toMutableList()
                newList.removeAll { it.userId == user.userId }
                participants.postValue(newList)
            }
            SocketEvent.RISE_A_HAND -> {
                if (currentUserId == user.userId) {
                    isHandRisen.postValue(true)
                } else {
                    updateParticipant(user)
                }
            }
            SocketEvent.REMOVE_HAND -> {
                if (currentUserId == user.userId) {
                    isHandRisen.postValue(false)
                } else {
                    updateParticipant(user)
                }
            }
            SocketEvent.REMOVED_FROM_GROUP -> {
                if (user.userId == currentUserId) {
                    finishCall()
                }
            }
            SocketEvent.SPEAK -> {
                if (currentUserId != user.userId) {
                    focusedUserId.postValue(user.userId.toString())
                }

                viewModelScope.launch {
                    delay(SPEECH_FOCUS_TIME)
                    focusedUserId.postValue(UNFOCUSED_USER_ID)
                }

                if (chatRole == ChatRole.OWNER) {
                    groupUseCase.lowerAHand(user.userId)
                }
            }
            SocketEvent.GROUP_STARTED -> {
                groupInfo.value?.let {
                    groupUseCase.updateGroupLocal(
                        it.copy(
                            GroupStatus.ACTIVE
                        )
                    )
                }
                refetchGroupInfo()
                changeState(VideoChatState.PROGRESS)
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

    private fun updateParticipant(participant: PresentationChatParticipant) {
        val newList = participants.value!!.toMutableList()
        val index = newList.indexOfFirst { it.userId == participant.userId }
        if (index != -1) {
            newList.removeAt(index)
            newList.add(index, participant)
            participants.postValue(newList)
        }
    }

    override fun init(groupId: Long) {
        this.groupId = groupId
        postDefaultValues()

        viewModelScope.launch {

            var groupEntity: GroupEntity? = null
            var creds: GroupCredentialsModel? = null
            var currentUser: UserEntity? = null

            val groupJob = launch {
                val result = groupUseCase.getGroupDetails(groupId)

                if (result.isSuccessful) {
                    groupEntity = result.successModel!!
                    groupInfo.value = groupEntity
                } else {
                    showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
                }
            }

            val userJob = launch {
                currentUser = userUseCase.getUser()
                currentUserId = currentUser!!.id
                val result = groupUseCase.getCredentials(groupId)

                if (result.isSuccessful) {
                    creds = result.successModel!!
                } else {
                    showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
                }
            }

            groupJob.join()
            userJob.join()

            initializeChatState(groupEntity, creds, currentUser)

            loadParticipants()

            delay(PARTICIPANTS_RESYNC_DELAY)
            loadParticipants()
        }

        changeState(VideoChatState.PREVIEW)
    }

    private fun postDefaultValues() {
        isStartButtonVisible.postValue(false)
        isVideoEnabled.postValue(true)
        isAudioEnabled.postValue(true)
        isHandRisen.postValue(false)
        isFinishing.postValue(false)
    }

    private fun loadParticipants() {
        viewModelScope.launch {
            val result = groupUseCase.getParticipantList(groupId, isConnected = true)

            if (result.isSuccessful) {
                val joinedUsers = result.successModel!!.filter { it.id != currentUserId }
                    .map {
                        var newItem = it.toPresentation()
                        if (chatRole != null) {
                            newItem = newItem.copy(
                                isHandRisen = newItem.isHandRisen && chatRole == ChatRole.OWNER
                            )
                        }
                        newItem
                    }
                participants.postValue(joinedUsers.mergeParticipants(participants.value))
            } else {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
        }
    }

    private fun List<PresentationChatParticipant>.mergeParticipants(localList: List<PresentationChatParticipant>?): List<PresentationChatParticipant> {
        val remoteList = this.toMutableList()
        localList?.forEach { localItem ->
            if (remoteList.find { it.userId == localItem.userId } == null) {
                remoteList.add(localItem)
            }
        }

        return remoteList
    }

    private fun initializeChatState(
        groupEntity: GroupEntity?,
        creds: GroupCredentialsModel?,
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
        router.navigateToPermissionsRequiredDialog(resultCode)
    }

    override fun forceDisconnect() {
        clearChatResources()
    }

    override fun finishCall() {
        clearChatResources()
        //todo send finish message to socket
        router.finishActivity()
    }

    override fun onOpenOptions() {
        if (chatRole != null) {
            if (chatRole == ChatRole.OWNER) {
                router.navigateToMMChatOptions()
            } else {
                router.navigateUserChatOptions()
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
        isAudioEnabled.switch()
    }

    override fun switchCamera() {
        if (hasBothCameras) {
            switchCameraEvent.call()
        }
    }

    override fun onOkClick() {
        router.finishActivity()
    }

    override fun onConnected(presentParticipantIds: List<String>) {
        presentParticipantIds.forEach {
            onUserConnected(it)
        }
    }

    override fun onUserConnected(id: String) {
        if (id == groupInfo.value?.owner?.id?.toString()) {
            _IsMMConnected = true
        }
    }

    override fun onUserDisconnected(id: String) {
        if (id == groupInfo.value?.owner?.id?.toString()) {
            _IsMMConnected = false
        }
    }

    override fun onSpeakerChanged(id: String?) {
        currentSpeakerId = id
    }

    override fun canFetchMMVideo(): Boolean {
        val isMMInfoAvailable = groupInfo.value?.owner != null
        val isRegular = chatRole == ChatRole.VISITOR

        return _IsMMConnected && isMMInfoAvailable && isRegular
    }

    override fun isSpeaker(id: String): Boolean {
        return id == currentSpeakerId
    }

    override fun onStartGroupClick() {
        groupUseCase.startGroup()
    }

    override fun reportGroupOwner(content: String) {
        groupInfo.value?.let {
            report(content, it.owner!!.id)
        }
    }

    override fun report(content: String, participantId: Long) {
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

    override fun onParticipantClick(id: Long) {
        if (chatRole == ChatRole.OWNER) {
            router.navigateToChatParticipantActions(id)
        }
    }

    override fun allowToSay(userId: Long) {
        groupUseCase.allowToSay(userId)
        router.onBack()
    }

    override fun removeChatParticipant(userId: Long) {
        groupUseCase.removeChatParticipant(userId)
        router.onBack()
    }

    override fun onBackClick() {
        router.onBack()
    }

    override fun attachments(groupId: Long) {
        router.navigateToAttachments(groupId)
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
                router.navigateToPreview()
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
                router.navigateToPreview()
            }
            VideoChatState.PROGRESS -> {
                router.navigateToChatInProgress()
            }
            VideoChatState.FINISHED -> {
                isFinishing.postValue(false)
                router.navigateToChatFinishScreen()
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
        const val UNFOCUSED_USER_ID = "-1"
        private const val SPEECH_FOCUS_TIME = 3 * 1000L
        private const val TIMER_PERIOD = 1000L
        private const val FINISHING_TIMER_PERIOD = 1 * 60 * 1000L //every minute
        private const val PARTICIPANTS_RESYNC_DELAY = 10 * 1000L
    }
}