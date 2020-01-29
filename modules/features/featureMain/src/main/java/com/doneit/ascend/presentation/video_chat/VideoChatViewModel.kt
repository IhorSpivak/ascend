package com.doneit.ascend.presentation.video_chat

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.dto.GroupCredentialsModel
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.doneit.ascend.presentation.utils.extensions.toMinutesFormat
import com.doneit.ascend.presentation.utils.extensions.toTimerFormat
import com.doneit.ascend.presentation.video_chat.finished.ChatFinishedContract
import com.doneit.ascend.presentation.video_chat.in_progress.ChatInProgressContract
import com.doneit.ascend.presentation.video_chat.in_progress.mm_options.MMChatOptionsContract
import com.doneit.ascend.presentation.video_chat.in_progress.user_actions.ChatParticipantActionsContract
import com.doneit.ascend.presentation.video_chat.in_progress.user_options.UserChatOptionsContract
import com.doneit.ascend.presentation.video_chat.preview.ChatPreviewContract
import com.twilio.video.CameraCapturer
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
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
    override val participants = MutableLiveData<List<SocketUserEntity>>(listOf())
    //endregion

    //region ui properties
    override val timerLabel = MutableLiveData<String>()
    override val isStartButtonEnabled = MutableLiveData<Boolean>()
    override val finishingLabel = MutableLiveData<String>()
    //endregion

    //region chat parameters
    override val hasActiveSpeaker = MutableLiveData<Boolean>()
    override val isVideoEnabled = MutableLiveData<Boolean>()
    override val isAudioEnabled = MutableLiveData<Boolean>()
    override val isRecordEnabled = MutableLiveData<Boolean>()
    override val isHandRisen = MutableLiveData<Boolean>()
    override val isFinishing = MutableLiveData<Boolean>()
    override val switchCameraEvent = SingleLiveManager(Unit)
    //endregion

    //region local
    private val messages = groupUseCase.messagesStream
    private lateinit var chatState: VideoChatState
    private lateinit var chatBehaviour: ChatBehaviour

    private var activeSpeakerID: String? = null
    private var groupId: Long = -1
    private var currentUserId: Long = -1
    private var downTimer: CountDownTimer? = null
    private var timer: Timer? = null
    private val hasBothCameras =
        CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.FRONT_CAMERA)
                && CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.BACK_CAMERA)
    //endregion

    private val messagesObserver = Observer<SocketEventEntity> { socketEvent ->
        val user = socketEvent.data
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
        }
    }

    private fun updateParticipant(participant: SocketUserEntity) {
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
                }
            }

            val userJob = launch {
                currentUser = userUseCase.getUser()
                currentUserId = currentUser!!.id
                val res = groupUseCase.getCredentials(groupId)

                if (res.isSuccessful) {
                    creds = res.successModel!!
                }
            }

            groupJob.join()
            userJob.join()

            if (groupEntity != null && creds != null) {
                chatBehaviour =
                    if (currentUser!!.isMasterMind && groupEntity!!.owner!!.id == currentUser!!.id) {
                        ChatBehaviour.OWNER
                    } else {
                        ChatBehaviour.VISITOR
                    }

                credentials.postValue(
                    StartVideoModel(
                        chatBehaviour,
                        creds!!.name,
                        creds!!.token,
                        CameraCapturer.CameraSource.FRONT_CAMERA
                    )
                )
                changeState(VideoChatState.PREVIEW_DATA_LOADED)
                messages.observeForever(messagesObserver)
            }
        }

        changeState(VideoChatState.PREVIEW)
    }

    private fun postDefaultValues() {
        hasActiveSpeaker.postValue(true)
        isStartButtonEnabled.postValue(false)
        isVideoEnabled.postValue(true)
        isAudioEnabled.postValue(true)
        isRecordEnabled.postValue(true)
        isHandRisen.postValue(false)
        isFinishing.postValue(false)
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
        if (groupInfo.value != null) {
            if (chatBehaviour == ChatBehaviour.OWNER) {
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

    override fun switchRecordState() {
        isRecordEnabled.switch()
    }

    override fun switchCamera() {
        if (hasBothCameras) {
            switchCameraEvent.call()
        }
    }

    override fun onOkClick() {
        router.finishActivity()
    }

    override fun onSpeakerChanged(id: String) {
        activeSpeakerID = id
        hasActiveSpeaker.postValue(true)
    }

    override fun onUserDisconnected(id: String): Boolean {
        return if (id == activeSpeakerID) {
            activeSpeakerID = null
            hasActiveSpeaker.postValue(false)
            true
        } else {
            false
        }
    }

    override fun onStartGroupClick() {
        changeState(VideoChatState.PROGRESS)
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
            && chatBehaviour == ChatBehaviour.OWNER
        ) {
            changeState(VideoChatState.MM_CONNECTION_LOST)
        }
    }

    override fun onParticipantClick(id: Long) {
        if (chatBehaviour == ChatBehaviour.OWNER) {
            router.navigateToChatParticipantActions(id)
        }
    }

    override fun removeChatParticipant(id: Long) {
        groupUseCase.removeChatParticipant(id)
    }

    override fun onBackClick() {
        router.onBack()
    }

    override fun onCleared() {
        messages.removeObserver(messagesObserver)
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
                router.navigateToPreview()
            }
            VideoChatState.PREVIEW_DATA_LOADED -> {
                initDownTimer(groupInfo.value!!)
            }
            VideoChatState.PREVIEW_GROUP_STARTED -> {
                initProgressTimer(groupInfo.value!!)
                if (chatBehaviour == ChatBehaviour.OWNER) {
                    isStartButtonEnabled.postValue(true)
                } else {
                    changeState(VideoChatState.PROGRESS)
                }
            }
            VideoChatState.MM_CONNECTION_LOST -> {
                router.navigateToPreview()
            }
            VideoChatState.PROGRESS -> {
                router.navigateToChatInProgress()
                groupUseCase.connectToChannel(groupId)
            }
            VideoChatState.FINISHED -> {
                router.navigateToChatFinishScreen()
                clearChatResources()
            }
        }
    }

    private fun initDownTimer(group: GroupEntity) {
        val currentDate = Date()
        downTimer?.cancel()
        downTimer = object : CountDownTimer(group.startTime!!.time - currentDate.time, 1000) {
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
                GroupEntity.PROGRESS_DURATION + group.startTime!!.time,
                MAIN_TIMER_PERIOD
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
        messages.removeObserver(messagesObserver)
    }

    companion object {
        private const val MAIN_TIMER_PERIOD = 1000L
        private const val FINISHING_TIMER_PERIOD = 1 * 60 * 1000L //every minute
    }
}