package com.doneit.ascend.presentation.video_chat_webinar

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.dto.GroupCredentialsDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.domain.entity.group.hourToMillis
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.Constants
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.doneit.ascend.presentation.utils.extensions.toMinutesFormat
import com.doneit.ascend.presentation.utils.extensions.toTimerFormat
import com.doneit.ascend.presentation.utils.extensions.toVideoChatTimerFormat
import com.doneit.ascend.presentation.video_chat.VideoChatActivity
import com.doneit.ascend.presentation.video_chat.VideoChatContract
import com.doneit.ascend.presentation.video_chat.states.ChatRole
import com.doneit.ascend.presentation.video_chat.states.ChatStrategy
import com.doneit.ascend.presentation.video_chat.states.VideoChatState
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.participant_options.ParticipantOptionsContract
import com.doneit.ascend.presentation.video_chat_webinar.preview.WebinarChatPreviewContract
import com.twilio.video.CameraCapturer
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask

class WebinarVideoChatViewModel(
    private val userUseCase: UserUseCase,
    private val groupUseCase: GroupUseCase
) : BaseViewModelImpl(), ParticipantOptionsContract.ViewModel,
    WebinarChatPreviewContract.ViewModel, WebinarVideoChatContract.ViewModel {

    override val groupInfo = MutableLiveData<GroupEntity>()
    override val navigation = SingleLiveEvent<WebinarVideoChatContract.Navigation>()


    //region ui properties
    override val timerLabel = MutableLiveData<String>()
    val isStartButtonVisible = MutableLiveData<Boolean>(false)
    override val finishingLabel = MutableLiveData<String>()
    //endregion

    //region chat parameters
    override val isFinishing = MutableLiveData<Boolean>()
    val switchCameraEvent = SingleLiveManager(Unit)
    //endregion

    //region local
    private lateinit var chatState: VideoChatState
    private var chatRole: ChatRole? = null
    private var chatStrategy = ChatStrategy.DOMINANT_SPEAKER

    private var groupId: Long = Constants.DEFAULT_MODEL_ID
    private var currentUserId: String = Constants.DEFAULT_MODEL_ID.toString()
    private var downTimer: CountDownTimer? = null
    private var timer: Timer? = null
    //endregion

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

        }

        changeState(VideoChatState.PREVIEW)
    }

    private fun postDefaultValues() {
        isStartButtonVisible.postValue(false)
        isFinishing.postValue(false)
    }

    //todo: this will needed in screen with all participants for group (or similar to this)
    private fun loadParticipants() {
        viewModelScope.launch {
            val result = groupUseCase.getParticipantList(groupId, isConnected = true)

            if (result.isSuccessful) {
                val participants = result.successModel!!.toMutableList()
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
            changeState(VideoChatState.PREVIEW_DATA_LOADED)
        }
    }

    fun onPermissionsRequired(resultCode: VideoChatActivity.ResultStatus) {
        VideoChatContract.Navigation.TO_PERMISSIONS_REQUIRED_DIALOG.data.putInt(
            ACTIVITY_RESULT_KEY,
            resultCode.ordinal
        )
        navigation.postValue(WebinarVideoChatContract.Navigation.TO_PERMISSIONS_REQUIRED_DIALOG)
    }

    override fun onOpenOptions() {
        if (chatRole != null) {
            if (chatRole == ChatRole.OWNER) {
                navigation.postValue(WebinarVideoChatContract.Navigation.TO_MM_CHAT_OPTIONS)
            } else {
                navigation.postValue(WebinarVideoChatContract.Navigation.TO_USER_CHAT_OPTIONS)
            }
        }
    }

    fun switchCamera() {
        if (cameraSources.size > 1) {
            switchCameraEvent.call()
        }
    }

    fun onOkClick() {
        navigation.postValue(WebinarVideoChatContract.Navigation.FINISH_ACTIVITY)
    }

    fun onStartGroupClick() {
        groupUseCase.startGroup()
    }

    override fun reportGroupOwner(content: String) {
        groupInfo.value?.let {
            report(content, it.owner!!.id.toString())
        }
    }

    override fun leaveGroup() {
        TODO("Not yet implemented")
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
            navigation.postValue(WebinarVideoChatContract.Navigation.TO_CHAT_PARTICIPANT_ACTIONS)
        }
    }

    override fun onBackClick() {
        navigation.postValue(WebinarVideoChatContract.Navigation.BACK)
    }

    override fun onNotesClick() {
        VideoChatContract.Navigation.TO_NOTES.data.putLong(GROUP_ID_KEY, groupId)
        navigation.postValue(WebinarVideoChatContract.Navigation.TO_NOTES)
    }

    override fun onChatClick() {
        TODO("Not yet implemented")
    }


    //region release resources
    override fun finishCall() {
        clearChatResources()
        navigation.postValue(WebinarVideoChatContract.Navigation.FINISH_ACTIVITY)
    }

    override fun onCleared() {
        clearChatResources()
        super.onCleared()
    }

    private fun clearChatResources() {
        groupUseCase.disconnect()
        downTimer?.cancel()
        timer?.cancel()
    }
    //endregion

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
                navigation.postValue(WebinarVideoChatContract.Navigation.TO_PREVIEW)
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
                navigation.postValue(WebinarVideoChatContract.Navigation.TO_PREVIEW)
            }
            VideoChatState.PROGRESS -> {
                navigation.postValue(WebinarVideoChatContract.Navigation.TO_CHAT_IN_PROGRESS)
            }
            VideoChatState.FINISHED -> {
                isFinishing.postValue(false)
                navigation.postValue(WebinarVideoChatContract.Navigation.TO_CHAT_FINISH)
                clearChatResources()
            }
        }
    }

    private fun initDownTimer(group: GroupEntity) {
        val currentDate = Date()
        downTimer?.cancel()
        downTimer =
            object :
                CountDownTimer(group.startTime!!.time - currentDate.time, TIMER_PERIOD) {
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
                    timerLabel.postValue(Date(group.timeInProgress).toVideoChatTimerFormat())
                }
            }.start()

        val finishingDate =
            Date(group.duration.hourToMillis() - GroupEntity.FINISHING_INTERVAL + group.startTime!!.time)
        timer?.cancel()
        timer = Timer()
        timer!!.schedule(timerTask {
            isFinishing.postValue(true)
        }, finishingDate)

        timer!!.schedule(timerTask {
            finishingLabel.postValue(Date(group.timeToFinish).toMinutesFormat())
        }, finishingDate, FINISHING_TIMER_PERIOD)
    }

    companion object {
        private const val SPEECH_FOCUS_TIME = 3 * 1000L
        private const val TIMER_PERIOD = 1000L
        private const val FINISHING_TIMER_PERIOD = 1 * 60 * 1000L //every minute
        private const val PARTICIPANTS_RESYNC_DELAY = 10 * 1000L

        const val GROUP_ID_KEY = "GROUP_ID_KEY"
        const val USER_ID_KEY = "USER_ID_KEY"
        const val ACTIVITY_RESULT_KEY = "ACTIVITY_RESULT_KEY"

        val cameraSources: List<CameraCapturer.CameraSource>

        init {
            val sources = mutableListOf<CameraCapturer.CameraSource>()

            if (CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.FRONT_CAMERA)) {
                sources.add(CameraCapturer.CameraSource.FRONT_CAMERA)
            }
            if (CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.BACK_CAMERA)) {
                sources.add(CameraCapturer.CameraSource.BACK_CAMERA)
            }

            cameraSources = sources
        }
    }

}
