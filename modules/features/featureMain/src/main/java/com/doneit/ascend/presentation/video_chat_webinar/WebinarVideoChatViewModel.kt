package com.doneit.ascend.presentation.video_chat_webinar

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.SocketEvent
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.domain.entity.dto.WebinarCredentialsDTO
import com.doneit.ascend.domain.entity.dto.WebinarQuestionDTO
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupStatus
import com.doneit.ascend.domain.entity.group.WebinarCredentialsEntity
import com.doneit.ascend.domain.entity.group.minutesToMillis
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.entity.webinar_question.QuestionSocketEntity
import com.doneit.ascend.domain.entity.webinar_question.QuestionSocketEvent
import com.doneit.ascend.domain.entity.webinar_question.WebinarQuestionEntity
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.domain.use_case.interactor.vimeo.VimeoUseCase
import com.doneit.ascend.domain.use_case.interactor.webinar_questions.WebinarQuestionUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.StartWebinarVideoModel
import com.doneit.ascend.presentation.models.toEntity
import com.doneit.ascend.presentation.utils.Constants
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.doneit.ascend.presentation.utils.extensions.toMinutesFormat
import com.doneit.ascend.presentation.utils.extensions.toTimerFormat
import com.doneit.ascend.presentation.utils.extensions.toVideoChatTimerFormat
import com.doneit.ascend.presentation.video_chat.delegates.VideoChatUtils
import com.doneit.ascend.presentation.video_chat.states.ChatRole
import com.doneit.ascend.presentation.video_chat.states.VideoChatState
import com.doneit.ascend.presentation.video_chat_webinar.delegate.vimeo.VimeoChatViewModelDelegate
import com.doneit.ascend.presentation.video_chat_webinar.finished.WebinarFinishedContract
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.WebinarVideoChatInProgressContract
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.owner_options.OwnerOptionsContract
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.participant_options.ParticipantOptionsContract
import com.doneit.ascend.presentation.video_chat_webinar.preview.WebinarChatPreviewContract
import com.doneit.ascend.presentation.video_chat_webinar.questions.QuestionContract
import com.twilio.video.CameraCapturer
import com.vrgsoft.networkmanager.livedata.SingleLiveEvent
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask


class WebinarVideoChatViewModel(
    private val userUseCase: UserUseCase,
    private val groupUseCase: GroupUseCase,
    private val webinarQuestionUseCase: WebinarQuestionUseCase,
    private val vimeoUseCase: VimeoUseCase
) : BaseViewModelImpl(), ParticipantOptionsContract.ViewModel,
    WebinarChatPreviewContract.ViewModel, WebinarVideoChatContract.ViewModel,
    OwnerOptionsContract.ViewModel, QuestionContract.ViewModel,
    WebinarVideoChatInProgressContract.ViewModel, WebinarFinishedContract.ViewModel {

    override val groupInfo = MutableLiveData<GroupEntity>()
    override val isVideoEnabled = MutableLiveData<Boolean>()
    override val isAudioRecording = MutableLiveData<Boolean>()
    override val isQuestionSent = MutableLiveData<Boolean>()
    override val isMMConnected: LiveData<Boolean> = MutableLiveData<Boolean>(false)
    override val isMuted = MutableLiveData<Boolean>()
    override val credentials = MutableLiveData<StartWebinarVideoModel>()
    override val isVisitor = MutableLiveData<Boolean>()

    override var viewModelDelegate: VimeoChatViewModelDelegate? = null

    override fun switchVideoEnabledState() {
        TODO("Not yet implemented")
    }

    override fun switchAudioEnabledState() {
        TODO("Not yet implemented")
    }

    override val navigation = SingleLiveEvent<WebinarVideoChatContract.Navigation>()


    //region ui properties
    override val timerLabel = MutableLiveData<String>()
    override val isStartButtonVisible = MutableLiveData<Boolean>(false)
    override val finishingLabel = MutableLiveData<String>()
    override val showMessgeSent = SingleLiveEvent<Void>()
    //endregion

    //region chat parameters
    override val isFinishing = MutableLiveData<Boolean>()
    override val switchCameraEvent = SingleLiveManager(Unit)
    //endregion

    //region local
    private lateinit var chatState: VideoChatState
    var chatRole: ChatRole? = null

    private var groupId: Long = Constants.DEFAULT_MODEL_ID
    private var currentUserId: String = Constants.DEFAULT_MODEL_ID.toString()
    private var downTimer: CountDownTimer? = null
    private var timer: Timer? = null

    private val messages = groupUseCase.messagesStream
    private val questionsStream = webinarQuestionUseCase.questionStream
    //endregion

    override val questions: LiveData<PagedList<WebinarQuestionEntity>> = groupInfo.switchMap {
        webinarQuestionUseCase.getWebinarQuestionLive(
            groupId, WebinarQuestionDTO(
                1,
                10
            )
        )
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
            webinarQuestionUseCase.removeQuestionsLocal()
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

            val creds = async {
                val result = groupUseCase.getWebinarCredentials(groupId)

                if (result.isSuccessful) {
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
        creds: WebinarCredentialsEntity?,
        currentUser: UserEntity?
    ) {

        if (groupEntity != null && creds != null) {
            if (viewModelDelegate == null)
                viewModelDelegate = VideoChatUtils.vimeoViewModelDelegate(this)
            //viewModelDelegate!!.initializeChatState(groupEntity, creds, currentUser)
                chatRole =
                    if (currentUser!!.isMasterMind && groupEntity.owner!!.id == currentUser.id) {
                        isVisitor.postValue(false)
                        ChatRole.OWNER
                    } else {
                        isVisitor.postValue(true)
                        ChatRole.VISITOR
                    }
                credentials.value =
                    StartWebinarVideoModel(
                        chatRole!!,
                        creds!!.chatId,
                        creds.key,
                        creds.link
                    )

                changeState(VideoChatState.PREVIEW_DATA_LOADED)

            messages.observeForever(groupObserver)
            questionsStream.observeForever(questionObserver)
        }
    }

    override fun onPermissionsRequired(resultCode: WebinarVideoChatActivity.ResultStatus) {
        WebinarVideoChatContract.Navigation.TO_PERMISSIONS_REQUIRED_DIALOG.data.putInt(
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

    override fun createQuestion(question: String) {
        viewModelScope.launch {
            val result = webinarQuestionUseCase.createQuestion(groupId, question)

            if (result.isSuccessful.not()) {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            } else {
                showMessgeSent.call()
            }
        }
    }

    override fun switchCamera() {
        if (viewModelDelegate?.getCameraCount() ?: 0 > 1) {
            switchCameraEvent.call()
        }
    }

    override fun onOkClick() {
        navigation.postValue(WebinarVideoChatContract.Navigation.FINISH_ACTIVITY)
    }

    override fun onStartGroupClick() {
        groupUseCase.startGroup()
    }

    override fun reportGroupOwner(content: String) {
        groupInfo.value?.let {
            report(content, it.owner!!.id.toString())
        }
    }

    override fun leaveGroup() {
        navigation.postValue(WebinarVideoChatContract.Navigation.FINISH_ACTIVITY)
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
            WebinarVideoChatContract.Navigation.TO_CHAT_PARTICIPANT_ACTIONS.data.putString(
                USER_ID_KEY,
                id
            )
            navigation.postValue(WebinarVideoChatContract.Navigation.TO_CHAT_PARTICIPANT_ACTIONS)
        }
    }

    override fun onBackClick() {
        navigation.postValue(WebinarVideoChatContract.Navigation.BACK)
    }

    override fun onNotesClick() {
        WebinarVideoChatContract.Navigation.TO_NOTES.data.putLong(GROUP_ID_KEY, groupId)
        navigation.postValue(WebinarVideoChatContract.Navigation.TO_NOTES)
    }

    override fun onChatClick() {
        credentials.value?.let {
            WebinarVideoChatContract.Navigation.TO_CHAT.data.putLong(CHAT_ID_KEY, it.chatId)
            navigation.postValue(WebinarVideoChatContract.Navigation.TO_CHAT)
        }

    }

    override fun onQuestionsClick() {
        navigation.postValue(WebinarVideoChatContract.Navigation.TO_QUESTIONS)
    }


    //region release resources
    override fun finishCall() {
        clearChatResources()
        navigation.postValue(WebinarVideoChatContract.Navigation.FINISH_ACTIVITY)
    }

    override fun onCleared() {
        clearChatResources()
        messages.removeObserver(groupObserver)
        questionsStream.removeObserver(questionObserver)
        super.onCleared()
    }

    private fun clearChatResources() {
        groupUseCase.disconnect()
        webinarQuestionUseCase.disconnect()
        viewModelDelegate?.clearChatResources()
        downTimer?.cancel()
        timer?.cancel()
    }
    //endregion

    private fun MutableLiveData<Boolean>.switch() {
        value?.let {
            postValue(!it)
        }
    }

    fun changeState(newState: VideoChatState) {
        chatState = newState
        when (chatState) {
            VideoChatState.PREVIEW -> {
                groupUseCase.connectToChannel(groupId)
                navigation.postValue(WebinarVideoChatContract.Navigation.TO_PREVIEW)
            }
            VideoChatState.PREVIEW_DATA_LOADED -> {
                initDownTimer(groupInfo.value!!)
                groupUseCase.participantConnectionStatus(currentUserId, true)
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
                //TODO:
                if (credentials.value?.key == null && credentials.value?.link == null) {
                    if (chatRole == ChatRole.OWNER) {
                        createLiveEvent()
                    } else {
                        // do nothing
                    }
                } else {
                    getM3u8Playback()
                }

                webinarQuestionUseCase.connectToChannel(groupId)
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
            Date(group.duration.minutesToMillis() - GroupEntity.FINISHING_INTERVAL + group.startTime!!.time)
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
        const val CHAT_ID_KEY = "CHAT_ID_KEY"
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

    private val questionObserver = Observer<QuestionSocketEntity?> { socketEvent ->
        socketEvent?.let {
            when (socketEvent.event) {
                QuestionSocketEvent.CREATE -> {
                    val question = socketEvent.toEntity()
                    question.let {
                        webinarQuestionUseCase.insertMessage(it)
                    }
                }
                QuestionSocketEvent.UPDATE -> {

                }
                QuestionSocketEvent.DESTROY -> {

                }
                else -> {
                    throw IllegalArgumentException("unknown socket type")
                }
            }
        }
    }

    private val groupObserver = Observer<SocketEventEntity> { socketEvent ->
        when (socketEvent.event) {
            SocketEvent.PARTICIPANT_CONNECTED -> {
                Log.d("socket", "connected")
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
            SocketEvent.PARTICIPANT_DISCONNECTED -> {
                Log.d("socket", "disconnected")
            }
            else -> Unit
        }
    }

    private fun createLiveEvent() {
        groupInfo.value?.name?.let {
            viewModelScope.launch {
                val res = vimeoUseCase.createLiveStream(it)
                if (res.isSuccessful) {
                    //TODO: refactor drop
                    delay(2000)
                    val updateResponse = vimeoUseCase.updateLiveStream(res.successModel?.link!!.drop(13).toLong())
                    if(updateResponse.isSuccessful) {
                        val response = groupUseCase.setWebinarCredentials(
                            groupInfo.value!!.id,
                            WebinarCredentialsDTO(
                                res.successModel?.streamKey,
                                res.successModel?.link
                            )
                        )
                        if (response.isSuccessful) {

                            val startModel = StartWebinarVideoModel(
                                credentials.value!!.role,
                                credentials.value!!.chatId,
                                res.successModel?.streamKey,
                                res.successModel?.link
                            )
                            credentials.value = startModel
                        }
                    }

                }

            }
        }
    }

    override fun getM3u8Playback(){
        viewModelScope.launch {
            val res = vimeoUseCase.getM3u8(credentials.value!!.link!!.drop(13).toLong())
            if(res.isSuccessful){
                Log.d("success", "success")
            } else {

            }
        }
    }




}
