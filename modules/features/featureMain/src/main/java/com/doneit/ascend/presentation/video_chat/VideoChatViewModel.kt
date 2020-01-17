package com.doneit.ascend.presentation.video_chat

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.SocketEvent
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.utils.toTimerFormat
import com.doneit.ascend.presentation.video_chat.in_progress.ChatInProgressContract
import com.doneit.ascend.presentation.video_chat.in_progress.mm_options.MMChatOptionsContract
import com.doneit.ascend.presentation.video_chat.in_progress.user_options.UserChatOptionsContract
import com.doneit.ascend.presentation.video_chat.preview.ChatPreviewContract
import com.twilio.video.CameraCapturer
import kotlinx.coroutines.launch
import java.util.*

class VideoChatViewModel(
    private val router: VideoChatContract.Router,
    private val userUseCase: UserUseCase,
    private val groupUseCase: GroupUseCase
) : BaseViewModelImpl(),
    VideoChatContract.ViewModel,
    ChatInProgressContract.ViewModel,
    ChatPreviewContract.ViewModel,
    UserChatOptionsContract.ViewModel,
    MMChatOptionsContract.ViewModel {

    override val credentials = MutableLiveData<StartVideoModel>()
    override val groupInfo = MutableLiveData<GroupEntity>()
    override val participants = MutableLiveData<List<SocketEventEntity>>(listOf())
    override val timer = MutableLiveData<String>()
    override val isVideoEnabled = MutableLiveData<Boolean>(true)
    override val isAudioEnabled = MutableLiveData<Boolean>(true)
    override val isRecordEnabled = MutableLiveData<Boolean>(true)
    override val messages = groupUseCase.messagesStream//todo remove in future

    private var groupId: Long = -1
    private var downTimer: CountDownTimer? = null
    private var currentUser: UserEntity? = null
    private lateinit var chatState: VideoChatState

    private val messagesObserver = Observer<SocketEventEntity> { socketEvent ->
        when (socketEvent.event) {
            SocketEvent.PARTICIPANT_CONNECTED -> {
                val newList = participants.value!!.toMutableList()
                newList.add(socketEvent)
                participants.postValue(newList)
            }
            SocketEvent.PARTICIPANT_DISCONNECTED -> {
                val newList = participants.value!!.toMutableList()
                newList.removeAll { it.userId == socketEvent.userId }
                participants.postValue(newList)
            }
        }
    }

    override fun init(groupId: Long) {
        this.groupId = groupId
        viewModelScope.launch {
            launch {
                currentUser = userUseCase.getUser()
                val creds = groupUseCase.getCredentials(groupId)

                credentials.postValue(
                    StartVideoModel(
                        currentUser!!.isMasterMind,
                        creds.successModel!!.name,
                        creds.successModel!!.token,
                        CameraCapturer.CameraSource.FRONT_CAMERA
                    )
                )
            }

            launch {
                val result = groupUseCase.getGroupDetails(groupId)

                if (result.isSuccessful) {
                    //result.successModel!!.startTime!!.time = Date().time + 15*1000
                    val groupEntity = result.successModel!!
                    groupInfo.postValue(groupEntity)
                    setInitialState(groupEntity)
                }
            }
        }

        messages.observeForever(messagesObserver)
        router.navigateToPreview()
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

    override fun onLeaveGroupClick() {
        router.finishActivity()
    }

    override fun onOpenOptions() {
        if (currentUser != null && groupInfo.value != null) {
            val isMasterMind =
                currentUser!!.isMasterMind && groupInfo.value?.owner?.id == currentUser?.id

            if (isMasterMind) {
                router.navigateToMMChatOptions()
            } else {
                router.navigateUserChatOptions()
            }
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

    private fun setInitialState(group: GroupEntity) {
        val currentDate = Date()
        if (currentDate.time < group.startTime!!.time) {
            changeState(VideoChatState.PREVIEW, group)
        } else if (group.inProgress) {
            changeState(VideoChatState.PROGRESS, group)
        } else {
            changeState(VideoChatState.FINISHED, group)
        }
    }

    private fun changeState(newState: VideoChatState, group: GroupEntity? = null) {
        chatState = newState
        val groupModel = group ?: groupInfo.value!!
        when (chatState) {
            VideoChatState.PREVIEW -> {
                initDownTimer(groupModel)
            }
            VideoChatState.PROGRESS -> {
                initProgressTimer(groupModel)
                groupUseCase.connectToChannel(groupId)
                router.navigateToChatInProgress()
            }
            VideoChatState.FINISHED -> {
                clearChatResources()
                router.navigateToChatFinishScreen()
            }
        }
    }

    private fun initDownTimer(group: GroupEntity) {
        val currentDate = Date()
        downTimer?.cancel()
        if (currentDate.time < group.startTime!!.time) {
            downTimer = object : CountDownTimer(group.startTime!!.time - currentDate.time, 1000) {
                override fun onFinish() {
                    changeState(VideoChatState.PROGRESS)
                }

                override fun onTick(p0: Long) {
                    timer.postValue("-" + Date(p0).toTimerFormat())
                }
            }.start()
        }
    }

    private fun initProgressTimer(group: GroupEntity) {
        downTimer?.cancel()

        downTimer =
            object : CountDownTimer(GroupEntity.PROGRESS_DURATION + group.startTime!!.time, 1000) {
                override fun onFinish() {
                    changeState(VideoChatState.FINISHED)
                }

                override fun onTick(p0: Long) {
                    timer.postValue(Date(group.timeInProgress).toTimerFormat())
                }
            }.start()
    }

    private fun clearChatResources() {
        groupUseCase.disconnect()
        downTimer?.cancel()
        messages.removeObserver(messagesObserver)
    }
}