package com.doneit.ascend.presentation.video_chat

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.StartVideoModel
import com.doneit.ascend.presentation.utils.toTimerFormat
import com.doneit.ascend.presentation.video_chat.in_progress.ChatInProgressContract
import com.doneit.ascend.presentation.video_chat.preview.ChatPreviewContract
import kotlinx.coroutines.launch
import java.util.*

class VideoChatViewModel(
    private val router: VideoChatContract.Router,
    private val userUseCase: UserUseCase,
    private val groupUseCase: GroupUseCase
) : BaseViewModelImpl(),
    VideoChatContract.ViewModel,
    ChatInProgressContract.ViewModel,
    ChatPreviewContract.ViewModel {

    override val credentials = MutableLiveData<StartVideoModel>()
    override val groupInfo = MutableLiveData<GroupEntity>()
    override val timer = MutableLiveData<String>()
    override val messages = groupUseCase.messagesStream

    private var groupId: Long = -1
    private var downTimer: CountDownTimer? = null
    private lateinit var chatState: VideoChatState

    override fun init(groupId: Long) {
        this.groupId = groupId
        viewModelScope.launch {
            launch {
                val user = userUseCase.getUser()
                val creds = groupUseCase.getCredentials(groupId)

                credentials.postValue(
                    StartVideoModel(
                        user!!.isMasterMind,
                        creds.successModel!!.name,
                        creds.successModel!!.token
                    )
                )
            }

           launch {
                val result = groupUseCase.getGroupDetails(groupId)

                if(result.isSuccessful) {
                    //result.successModel!!.startTime!!.time = Date().time + 15*1000
                    val groupEntity = result.successModel!!
                    groupInfo.postValue(groupEntity)
                    setInitialState(groupEntity)
                }
            }
        }

        router.navigateToPreview()
    }

    override fun onPermissionsRequired(resultCode: VideoChatActivity.ResultStatus) {
        router.navigateToPermissionsRequiredDialog(resultCode)
    }

    override fun forceDisconnect() {
        groupUseCase.disconnect()
    }

    override fun onBackClick() {
        router.onBack()
    }

    private fun setInitialState(group: GroupEntity) {
        val currentDate = Date()
        if(currentDate.time < group.startTime!!.time) {
            changeState(VideoChatState.PREVIEW, group)
        } else if(group.inProgress) {
            changeState(VideoChatState.PROGRESS, group)
        } else {
            changeState(VideoChatState.FINISHED, group)
        }
    }

    private fun changeState(newState: VideoChatState, group: GroupEntity? = null) {
        chatState = newState
        val groupModel = group ?: groupInfo.value!!
        when(chatState) {
            VideoChatState.PREVIEW -> {
                initDownTimer(groupModel)
            }
            VideoChatState.PROGRESS -> {
                initProgressTimer(groupModel)
                groupUseCase.connectToChannel(groupId)
                router.navigateToChatInProgress()
            }
            VideoChatState.FINISHED -> {
                downTimer?.cancel()
                groupUseCase.disconnect()
                router.navigateToChatFinishScreen()
            }
        }
    }

    private fun initDownTimer(group: GroupEntity) {
        val currentDate = Date()
        downTimer?.cancel()
        if(currentDate.time < group.startTime!!.time) {
            downTimer = object: CountDownTimer(group.startTime!!.time - currentDate.time, 1000) {
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

        downTimer = object: CountDownTimer(GroupEntity.PROGRESS_DURATION + group.startTime!!.time, 1000) {
            override fun onFinish() {
                changeState(VideoChatState.FINISHED)
            }

            override fun onTick(p0: Long) {
                timer.postValue(Date(group.timeInProgress).toTimerFormat())
            }
        }.start()
    }
}