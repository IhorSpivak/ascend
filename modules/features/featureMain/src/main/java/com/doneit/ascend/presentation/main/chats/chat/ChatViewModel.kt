package com.doneit.ascend.presentation.main.chats.chat

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.ChatSocketEvent
import com.doneit.ascend.domain.entity.MessageSocketEntity
import com.doneit.ascend.domain.entity.chats.*
import com.doneit.ascend.domain.entity.dto.MessageDTO
import com.doneit.ascend.domain.entity.dto.MessageListDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.chats.chat.common.ChatType
import com.doneit.ascend.presentation.models.chat.ChatWithUser
import com.doneit.ascend.presentation.models.toEntity
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class ChatViewModel(
    private val chatUseCase: ChatUseCase,
    private val userUseCase: UserUseCase,
    private val router: ChatContract.Router,
    private val localRouter: ChatContract.LocalRouter,
    private val groupUseCase: GroupUseCase
) : BaseViewModelImpl(), ChatContract.ViewModel {
    override val membersCountGroup: MutableLiveData<Int> = MutableLiveData()
    override val chat: MediatorLiveData<ChatWithUser> = MediatorLiveData()
    override var chatType: ChatType = ChatType.CHAT
    override val chatModel: MutableLiveData<ChatEntity> = MutableLiveData()

    private val searchQuery = MutableLiveData<String>()

    override val canAddMember: Boolean
        get() = (selectedMembers.size + chat.value!!.chat.members!!.size) < 50

    override val selectedMembers: MutableList<AttendeeEntity> = mutableListOf()

    override val searchResult: LiveData<PagedList<AttendeeEntity>>
        get() = searchQuery.switchMap {
            groupUseCase.searchMembers(
                it,
                user.value!!.id,
                chat.value!!.chat.members?.filter { !it.removed }?.map { member -> member.toAttendeeEntity() })
        }

    //don't used
    override val addedMembers: LiveData<List<AttendeeEntity>> = MutableLiveData()

    private val socketMessage = chatUseCase.messagesStream
    private lateinit var observer: Observer<MessageSocketEntity?>
    override val user = userUseCase.getUserLive()
    override val messages: LiveData<PagedList<MessageEntity>> = chatModel.switchMap {
        chatUseCase.getMessageList(
            it.id, MessageListDTO(
                perPage = 50,
                sortColumn = "created_at",
                sortType = SortType.DESC
            )
        )
    }

    init {
        chat.addSource(user) {
            applyData(chatModel.value, it)
        }

        chat.addSource(chatModel) {
            applyData(it, user.value)
        }

        viewModelScope.launch { initRequestCycle() }
    }

    private suspend fun initRequestCycle() {
        while (true) {
            delay(5000)
            chatModel.value = chatModel.value
        }
    }

    override fun applyData(chatEntity: ChatEntity?, user: UserEntity?) {

        if (chatEntity != null && user != null) {
            chat.postValue(
                ChatWithUser(
                    chatEntity,
                    user,
                    chatType
                )
            )
        }
    }

    override fun setChat(id: Long) {
        chatUseCase.connectToChannel(id)
        observer = getMessageObserver(id)
        initMessageStream()
        viewModelScope.launch {
            val response = chatUseCase.getChatDetails(id)

            if (response.isSuccessful) {
                response.successModel?.let {
                    chatModel.postValue(it)
                }
            }
        }
    }

    override fun initMessageStream() {
        socketMessage.observeForever(observer)
    }

    override fun onBackPressed() {
        router.onBack()
    }

    override fun onLocalBackPressed() {
        if (selectedMembers.isNotEmpty()) {
            viewModelScope.launch {
                val response = chatUseCase.updateChat(
                    chat.value!!.chat.id,
                    null,
                    selectedMembers.map { it.id })
                if (response.isSuccessful) {
                    response.successModel?.membersCount?.let {
                        membersCountGroup.postValue(it)
                        chatModel.postValue(response.successModel)
                    }
                }
                clearResources()
                localRouter.onBack()
            }
        } else {
            clearResources()
            localRouter.onBack()
        }
    }

    override fun onAddMember(member: AttendeeEntity) {
        selectedMembers.add(member)
    }

    override fun onRemoveMember(member: AttendeeEntity) {
        selectedMembers.firstOrNull { it.id == member.id }?.let {
            selectedMembers.remove(it)
        }
    }

    override fun onQueryTextChange(query: String) {
        searchQuery.postValue(query)
    }

    override fun updateChatName(newName: String) {
        viewModelScope.launch {
            val response = chatUseCase.updateChat(chatModel.value!!.id, title = newName)
            if (response.isSuccessful) {
                chatModel.postValue(response.successModel)
            }
        }
    }

    override fun inviteUser() {
        localRouter.navigateToInviteChatMember()
    }

    override fun sendMessage(message: String) {
        viewModelScope.launch {
            chatUseCase.sendMessage(MessageDTO(chatModel.value!!.id, message))
        }
    }

    override fun onChatDetailsClick() {
        chat.value?.let { chatWithUser ->
            if (chatWithUser.chat.members?.size == ChatFragment.PRIVATE_CHAT_MEMBER_COUNT) {
                router.goToDetailedUser(chatWithUser.chat.members?.firstOrNull {
                    it.id != chatWithUser.user.id && !it.removed && !it.leaved
                }?.id ?: return@let)
            } else {
                user.value?.let {
                    router.goToChatMembers(
                        chatWithUser.chat.id,
                        chatWithUser.chat.members.orEmpty().filter {
                            !it.leaved && !it.removed
                        }, it
                    )
                }
            }
        }
    }

    override fun onImageClick() {
        onChatDetailsClick()
    }

    override fun onBlockUserClick(member: MemberEntity) {
        viewModelScope.launch {
            chatUseCase.blockUser(member.id).let {
                if (it.isSuccessful) {
                    user.value?.let { user ->
                        userUseCase.update(user.apply { blockedUsersCount += 1 })
                        chatUseCase.addBlockedUser(member.toBlockedUser())
                    }
                    router.onBack()
                }
            }
        }
    }

    override fun showDetailedUser(userId: Long) {
        router.goToDetailedUser(userId)
    }

    override fun onUnblockUserClick(member: MemberEntity) {
        viewModelScope.launch {
            chatUseCase.unblockUser(member.id).let {
                if (it.isSuccessful) {
                    user.value?.let { user ->
                        userUseCase.update(user.apply { blockedUsersCount -= 1 })
                        chatUseCase.removeBlockedUser(member.toBlockedUser())
                    }
                    router.onBack()
                }
            }
        }
    }

    override fun onDelete(message: MessageEntity) {
        viewModelScope.launch {
            chatUseCase.removeMessageRemote(message.id).let {
                if (it.isSuccessful) {
                    chatUseCase.removeMessageLocal(message.id)
                }
            }
        }
    }

    override fun onDeleteChat() {
        viewModelScope.launch {
            chatModel.value?.let { chat ->
                chatUseCase.delete(chat.id).let {
                    if (it.isSuccessful.not()) {
                        showDefaultErrorMessage(it.errorModel!!.toErrorMessage())
                    } else {
                        router.onBack()
                    }
                }
            }
        }
    }

    override fun onReportChatOwner(content: String) {
        viewModelScope.launch {
            chatModel.value?.let { chat ->
                userUseCase.report(content, chat.chatOwnerId.toString()).let {
                    if (it.isSuccessful.not()) {
                        showDefaultErrorMessage(it.errorModel!!.toErrorMessage())
                    }
                }
            }
        }
    }

    override fun onReport(content: String, id: Long) {
        viewModelScope.launch {
            userUseCase.report(content, id.toString()).let {
                if (it.isSuccessful.not()) {
                    showDefaultErrorMessage(it.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun onLeave() {
        viewModelScope.launch {
            chatModel.value?.let { chat ->
                chatUseCase.leave(chat.id).let {
                    if (it.isSuccessful.not()) {
                        showDefaultErrorMessage(it.errorModel!!.toErrorMessage())
                    } else {
                        router.onBack()
                    }
                }
            }
        }
    }

    override fun markMessageAsRead(message: MessageEntity) {
        if (message.userId != user.value!!.id && message.status != MessageStatus.READ && message.isMarkAsReadSentToApprove.not()) {
            message.isMarkAsReadSentToApprove = true
            viewModelScope.launch {
                chatUseCase.markMessageAsRead(message.id).let {
                    if (it.isSuccessful.not()) {
                        showDefaultErrorMessage(it.errorModel!!.toErrorMessage())
                    }
                }
            }
        }
    }

    override fun onCleared() {
        socketMessage.removeObserver(observer)
        chatUseCase.disconnect()
        super.onCleared()
    }

    private fun getMessageObserver(chatId: Long): Observer<MessageSocketEntity?> {
        return Observer { socketEvent ->
            socketEvent?.let {
                when (socketEvent.event) {
                    ChatSocketEvent.MESSAGE -> {
                        val message = socketEvent.toEntity()
                        message.let {
                            chatUseCase.insertMessage(message, chatId)
                            messages.value?.dataSource?.invalidate()
                        }
                    }
                    ChatSocketEvent.DESTROY -> {
                        viewModelScope.launch {
                            chatUseCase.removeMessageLocal(socketEvent.id)
                        }
                    }
                    ChatSocketEvent.READ -> {
                        viewModelScope.launch {
                            chatUseCase.markMessageAsReadLocal(socketEvent.id)
                        }
                    }
                    else -> {
                        throw IllegalArgumentException("unknown socket type")
                    }
                }
            }
        }
    }

    private fun clearResources() {
        selectedMembers.clear()
        searchResult.value?.clear()
        searchQuery.postValue("")
    }

    private fun MemberEntity.toBlockedUser(): BlockedUserEntity {
        return BlockedUserEntity(
            id,
            fullName,
            image
        )
    }

    private fun MemberEntity.toAttendeeEntity(): AttendeeEntity {
        return AttendeeEntity(
            id,
            fullName,
            null,
            null,
            false
        )
    }
}