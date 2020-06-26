package com.doneit.ascend.presentation.video_chat_webinar

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.ChatSocketEvent
import com.doneit.ascend.domain.entity.MessageSocketEntity
import com.doneit.ascend.domain.entity.SocketEvent
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
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
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.domain.use_case.interactor.vimeo.VimeoUseCase
import com.doneit.ascend.domain.use_case.interactor.webinar_questions.WebinarQuestionUseCase
import com.doneit.ascend.presentation.common.LockableLiveData
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.chats.chat.livestream_user_actions.LivestreamUserActionsContract
import com.doneit.ascend.presentation.models.StartWebinarVideoModel
import com.doneit.ascend.presentation.models.group.WebinarChatParticipant
import com.doneit.ascend.presentation.models.toEntity
import com.doneit.ascend.presentation.models.toWebinarPresentation
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
    private val chatUseCase: ChatUseCase,
    private val webinarQuestionUseCase: WebinarQuestionUseCase,
    private val vimeoUseCase: VimeoUseCase
) : BaseViewModelImpl(), ParticipantOptionsContract.ViewModel,
    WebinarChatPreviewContract.ViewModel, WebinarVideoChatContract.ViewModel,
    OwnerOptionsContract.ViewModel, QuestionContract.ViewModel,
    WebinarVideoChatInProgressContract.ViewModel, WebinarFinishedContract.ViewModel,
    LivestreamUserActionsContract.ViewModel {

    override val groupInfo = MutableLiveData<GroupEntity>()
    override val isVideoEnabled = MutableLiveData(true)
    override val isAudioRecording = MutableLiveData(true)
    override val isQuestionSent = MutableLiveData<Boolean>()
    override val isMMConnected = MutableLiveData<Boolean>()
    override val isMuted = MutableLiveData<Boolean>()
    override val hasUnreadQuestion = LockableLiveData(false)
    override val hasUnreadMessage = MutableLiveData(false)

    override fun lockQuestionObserver() {
        hasUnreadQuestion.setValue(false)
        hasUnreadQuestion.lock()
    }

    override fun unlockQuestionObserver() {
        hasUnreadQuestion.unlock()
    }

    override val credentials = MutableLiveData<StartWebinarVideoModel>()
    override val isVisitor = MutableLiveData<Boolean>()
    override val participantsCount = MutableLiveData(0)
    override val participants = MutableLiveData<Set<WebinarChatParticipant>>(setOf())

    override var viewModelDelegate: VimeoChatViewModelDelegate? = null

    override fun switchVideoEnabledState() {
        isVideoEnabled.switch()
    }

    override fun switchAudioEnabledState() {
        isAudioRecording.switch()
    }

    override val navigation = SingleLiveEvent<WebinarVideoChatContract.Navigation>()
    override val m3u8url = MutableLiveData<String>()

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

    private val messages = chatUseCase.messagesStream
    private val groupEvents = groupUseCase.messagesStream
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

    private fun getParticipants() {
        viewModelScope.launch {
            val res = groupUseCase.getParticipantList(groupId)
            if (res.isSuccessful) {

                res.successModel!!.filter { it.isConnected }.also {
                    participants.value = it.map { it.toWebinarPresentation() }.toSet()
                    participantsCount.postValue(it.size)
                }
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
        getParticipants()
        postDefaultValues()

        viewModelScope.launch {
            val groupEntity = async {
                val result = groupUseCase.getGroupDetails(groupId)

                if (result.isSuccessful) {
                    groupInfo.value = result.successModel!!
                    isMMConnected.value = result.successModel!!.owner?.connected
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


    private fun initializeChatState(
        groupEntity: GroupEntity?,
        creds: WebinarCredentialsEntity?,
        currentUser: UserEntity?
    ) {

        if (groupEntity != null && creds != null) {
            if (viewModelDelegate == null)
                viewModelDelegate = VideoChatUtils.vimeoViewModelDelegate(this)
            webinarQuestionUseCase.removeQuestionsLocalExcept(groupEntity.id)
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
                    creds.chatId,
                    creds.key,
                    creds.link
                )
            credentials.value?.let {
                viewModelScope.launch {
                    val result = chatUseCase.getChatDetails(it.chatId)
                    if (result.isSuccessful) {
                        chatUseCase.connectToChannel(it.chatId)
                    }
                }

            }

            changeState(VideoChatState.PREVIEW_DATA_LOADED)
            groupEvents.observeForever(groupObserver)
            messages.observeForever(messagesObserver)
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
        switchCameraEvent.call()
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

    override fun remove(userId: Long) {
        groupUseCase.removeChatParticipant(userId.toString())
        onBackClick()
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

    override fun showAttendees() {
        if (chatRole == ChatRole.OWNER) {
            navigation.postValue(WebinarVideoChatContract.Navigation.TO_ATTENDEES)
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
            hasUnreadMessage.value = false
            WebinarVideoChatContract.Navigation.TO_CHAT.data.putLong(CHAT_ID_KEY, it.chatId)
            navigation.postValue(WebinarVideoChatContract.Navigation.TO_CHAT)
        }

    }

    override fun onQuestionsClick() {
        lockQuestionObserver()
        navigation.postValue(WebinarVideoChatContract.Navigation.TO_QUESTIONS)
    }


    //region release resources
    override fun finishCall() {
        navigation.postValue(WebinarVideoChatContract.Navigation.FINISH_ACTIVITY)
    }

    override fun onCleared() {
        groupEvents.removeObserver(groupObserver)
        messages.removeObserver(messagesObserver)
        questionsStream.removeObserver(questionObserver)
        clearChatResources()
        super.onCleared()
    }

    private fun clearChatResources() {
        groupUseCase.participantConnectionStatus(currentUserId, false)
        groupUseCase.disconnect()
        webinarQuestionUseCase.disconnect()
        chatUseCase.disconnect()
        viewModelDelegate?.clearChatResources()
        downTimer?.cancel()
        timer?.cancel()
    }
    //endregion

    fun changeState(newState: VideoChatState) {
        chatState = newState
        when (chatState) {
            VideoChatState.PREVIEW -> {
                groupUseCase.connectToChannel(groupId)
                webinarQuestionUseCase.connectToChannel(groupId)
                viewModelScope.launch {
                    //todo: find a solution without delay
                    delay(1000)
                    groupUseCase.participantConnectionStatus(currentUserId, true)
                }
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
                //TODO:
                if (chatRole == ChatRole.OWNER) {
                    if (credentials.value?.link == null) {
                        if (chatRole == ChatRole.OWNER) createLiveEvent()
                    } else if (credentials.value?.key == null) {
                        if (chatRole == ChatRole.OWNER)
                            viewModelScope.launch {
                                activateLiveStream(credentials.value?.link!!)
                            }
                    }
                }
                if (chatRole == ChatRole.VISITOR) {
                    if (credentials.value?.link != null && credentials.value?.link != null) {
                        getM3u8Playback()
                    } else {
                        getCredentials()
                    }
                }
                navigation.postValue(WebinarVideoChatContract.Navigation.TO_CHAT_IN_PROGRESS)
            }

            VideoChatState.FINISHED -> {
                isFinishing.postValue(false)
                navigation.postValue(WebinarVideoChatContract.Navigation.TO_CHAT_FINISH)
            }
        }
    }

    private fun getCredentials() {
        viewModelScope.launch {
            val result = groupUseCase.getWebinarCredentials(groupId)
            if (result.isSuccessful) {
                result.successModel?.let {
                    if (it.link != null && it.key != null) {
                        credentials.value =
                            StartWebinarVideoModel(
                                credentials.value!!.role,
                                credentials.value!!.chatId,
                                link = it.link,
                                key = it.key
                            )
                        getM3u8Playback()
                    } else {
                        delay(CREDS_RETRY_DELAY)
                        getCredentials()
                    }
                }
            } else {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
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

    private val questionObserver = Observer<QuestionSocketEntity?> { socketEvent ->
        socketEvent?.let {
            when (socketEvent.event) {
                QuestionSocketEvent.CREATE -> {
                    val question = socketEvent.toEntity()
                    question.let {
                        hasUnreadQuestion.postValue(true)
                        webinarQuestionUseCase.insertMessage(it)
                        questions.value?.dataSource?.invalidate()
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

    private val groupObserver = Observer<SocketEventEntity?> { socketEvent ->
        socketEvent?.let {
            val user = socketEvent.data.toWebinarPresentation()
            when (socketEvent.event) {
                SocketEvent.PARTICIPANT_CONNECTED -> {
                    if (user.userId == groupInfo.value?.owner?.id.toString()) {
                        viewModelScope.launch {
                            delay(LIVESTREAM_DELAY)
                            if(groupInfo.value?.owner?.id.toString() != currentUserId) {
                                getM3u8Playback()
                            }
                            isMMConnected.value = true
                        }
                    } else {
                        participants.value = participants.value!!.plus(user)
                        participantsCount.value = participants.value!!.size
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
                SocketEvent.PARTICIPANT_DISCONNECTED -> {
                    if (user.userId == groupInfo.value?.owner?.id.toString()) {
                        viewModelScope.launch {
                            delay(LIVESTREAM_DELAY)
                            isMMConnected.value = false
                        }
                    } else {
                        participants.value = participants.value!!.minus(user)
                        participantsCount.value = participants.value!!.size
                    }
                }
                SocketEvent.REMOVED_FROM_GROUP -> {
                    if (user.userId == currentUserId) {
                        finishCall()
                    } else {
                        participants.value = participants.value!!.minus(user)
                        participantsCount.value = participants.value!!.size
                    }
                }
                else -> Unit
            }
        }
    }


    private val messagesObserver = Observer<MessageSocketEntity?> { socketEvent ->
        when (socketEvent?.event) {
            ChatSocketEvent.MESSAGE -> {
                hasUnreadMessage.postValue(true)
            }
            null -> {
                hasUnreadMessage.postValue(false)
            }
            else -> Unit
        }
    }

    private fun createLiveEvent() {
        groupInfo.value?.name?.let {
            viewModelScope.launch {
                val res = vimeoUseCase.createLiveStream(it)
                if (res.isSuccessful) {
                    val response = setWebinarCredentials(
                        WebinarCredentialsDTO(
                            link = res.successModel!!.link
                        )
                    )
                    if (response.isSuccessful) {
                        activateLiveStream(res.successModel?.link!!)
                    }
                }
            }
        }
    }

    private suspend fun activateLiveStream(streamLink: String) {
        val streamId = getStreamId(streamLink);
        val activateResponse =
            vimeoUseCase.activateLiveStream(streamId)
        if (activateResponse.isSuccessful) {
            val response = setWebinarCredentials(
                WebinarCredentialsDTO(
                    key = activateResponse.successModel?.streamKey,
                    link = streamLink
                )
            )
        } else {
            delay(ACTIVATE_LIVESTREAM_DELAY)
            activateLiveStream(streamLink)
        }
    }

    override fun getM3u8Playback() {
        credentials.value?.link?.let {
            viewModelScope.launch {
                val res = vimeoUseCase.getM3u8(getStreamId(it))
                if (res.isSuccessful) {
                    m3u8url.postValue(res.successModel!!.m3u8playbackUrl)
                } else {
                    delay(M3U8_RETRY_DELAY)
                    getM3u8Playback()
                }
            }
        }

    }

    private suspend fun setWebinarCredentials(credentialsDTO: WebinarCredentialsDTO): ResponseEntity<Unit, List<String>> {
        val response = groupUseCase.setWebinarCredentials(
            groupInfo.value!!.id,
            credentialsDTO
        )
        if (response.isSuccessful) {
            val startModel = StartWebinarVideoModel(
                credentials.value!!.role,
                credentials.value!!.chatId,
                link = credentialsDTO.link,
                key = credentialsDTO.key
            )
            credentials.value = startModel
        }
        return response
    }

    private fun getStreamId(streamLink: String): Long {
        //TODO: refactor drop
        return streamLink.drop(13).toLong()
    }

    private fun MutableLiveData<Boolean>.switch() {
        value?.let {
            postValue(!it)
        }
    }

    companion object {
        private const val TIMER_PERIOD = 1000L
        private const val FINISHING_TIMER_PERIOD = 1 * 60 * 1000L //every minute
        private const val LIVESTREAM_DELAY = 13000L
        private const val M3U8_RETRY_DELAY = 5000L
        private const val CREDS_RETRY_DELAY = 2000L
        private const val ACTIVATE_LIVESTREAM_DELAY = 2000L

        const val GROUP_ID_KEY = "GROUP_ID_KEY"
        const val CHAT_ID_KEY = "CHAT_ID_KEY"
        const val USER_ID_KEY = "USER_ID_KEY"
        const val ACTIVITY_RESULT_KEY = "ACTIVITY_RESULT_KEY"
    }
}
