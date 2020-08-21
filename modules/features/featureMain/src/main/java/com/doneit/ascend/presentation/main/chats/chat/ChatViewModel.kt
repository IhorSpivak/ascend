package com.doneit.ascend.presentation.main.chats.chat

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.ChatSocketEvent
import com.doneit.ascend.domain.entity.MessageAttachment
import com.doneit.ascend.domain.entity.MessageSocketEntity
import com.doneit.ascend.domain.entity.chats.*
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.community_feed.Size
import com.doneit.ascend.domain.entity.dto.MessageDTO
import com.doneit.ascend.domain.entity.dto.MessageListDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.group.GroupEntity
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class ChatViewModel(
    private val chatUseCase: ChatUseCase,
    private val userUseCase: UserUseCase,
    private val router: ChatContract.Router,
    private val localRouter: ChatContract.LocalRouter,
    private val groupUseCase: GroupUseCase,
    private val chatWithUser: ChatWithUser
) : BaseViewModelImpl(), ChatContract.ViewModel {

    override val membersCountGroup: MutableLiveData<Int> = MutableLiveData()
    override val chat = MutableLiveData(chatWithUser)

    private val searchQuery = MutableLiveData<String>()

    override val canAddMember: Boolean
        get() = (selectedMembers.size + chatWithUser.chat.members.size) < 50

    override val selectedMembers: MutableList<AttendeeEntity> = mutableListOf()

    override val searchResult: LiveData<PagedList<AttendeeEntity>>
        get() = searchQuery.switchMap {
            groupUseCase.searchMembers(
                it,
                chatWithUser.user.id,
                chatWithUser.chat.members.filter { !it.removed }
                    .map { member -> member.toAttendeeEntity() })
        }

    //don't used
    override val addedMembers: LiveData<List<AttendeeEntity>> = MutableLiveData()

    private val socketMessage = chatUseCase.messagesStream
    private var observer: Observer<MessageSocketEntity?>
    override val messages: LiveData<PagedList<MessageEntity>> = chatUseCase.getMessageList(
        chatWithUser.chat.id, MessageListDTO(
            perPage = 50,
            sortColumn = "created_at",
            sortType = SortType.DESC
        )
    )

    init {
        viewModelScope.launch { initRequestCycle() }
        chatUseCase.connectToChannel(chatWithUser.chat.id)
        observer = getMessageObserver(chatWithUser.chat.id)
        initMessageStream()
    }

    private suspend fun initRequestCycle() {
        while (true) {
            delay(20000)
            refreshModel()
        }
    }

    private suspend fun refreshModel() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = chatUseCase.getChatDetails(
                chatWithUser.chat.id,
                chatWithUser.chatType == ChatType.CHAT
            )
            if (response.isSuccessful) {
                chatWithUser.chat.inheritFrom(response.successModel!!)
                chat.postValue(chatWithUser)
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
                    chatWithUser.chat.id,
                    null,
                    selectedMembers.map { it.id })
                if (response.isSuccessful) {
                    response.successModel?.let {
                        membersCountGroup.postValue(it.membersCount)
                        chatWithUser.chat.inheritFrom(it)
                        chat.value = chatWithUser
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
            val response = chatUseCase.updateChat(chatWithUser.chat.id, title = newName)
            if (response.isSuccessful) {
                chatWithUser.chat.title = newName
            }
        }
    }

    override fun inviteUser() {
        localRouter.navigateToInviteChatMember()
    }

    override fun sendMessage(message: String, attachmentType: String, attachmentUrl: String) {
        viewModelScope.launch {
            chatUseCase.sendMessage(
                MessageDTO(
                    chatWithUser.chat.id,
                    message,
                    attachmentUrl,
                    attachmentType
                )
            )
        }
    }

    override fun onChatDetailsClick() {
        if (chatWithUser.chat.members.size == ChatFragment.PRIVATE_CHAT_MEMBER_COUNT) {
            router.goToDetailedUser(chatWithUser.chat.members.firstOrNull {
                it.id != chatWithUser.user.id && !it.removed && !it.leaved
            }?.id ?: return)
        } else {
            router.goToChatMembers(
                chatWithUser.chat.id,
                chatWithUser.chat.chatOwnerId,
                chatWithUser.chat.chatType,
                chatWithUser.chat.members.filter {
                    !it.leaved && !it.removed
                }, chatWithUser.user
            )
        }
    }

    override fun onImageClick() {
        onChatDetailsClick()
    }

    override fun onBlockUserClick(member: MemberEntity) {
        viewModelScope.launch {
            chatUseCase.blockUser(member.id).let {
                if (it.isSuccessful) {
                    userUseCase.update(chatWithUser.user.apply { blockedUsersCount += 1 })
                    chatUseCase.addBlockedUser(member.toBlockedUser())
                    router.onBack()
                }
            }
        }
    }

    override fun showDetailedUser(userId: Long) {
        router.goToDetailedUser(userId)
    }

    override fun goToUserList(channel: ChatEntity) {
        router.goToChatMembers(
            chatWithUser.chat.id,
            chatWithUser.chat.chatOwnerId,
            chatWithUser.chat.chatType,
            chatWithUser.chat.members.filter {
                !it.leaved && !it.removed
            }, chatWithUser.user
        )
    }

    override fun showPostDetails(post: Post) {
        router.navigateToPostDetails(chatWithUser.user, post)
    }

    override fun showLiveStreamUser(member: MemberEntity) {
        router.goToLiveStreamUser(member)
    }

    override fun showGroup(group: GroupEntity) {
        router.navigateToDetails(group)
    }

    override fun goToEditChannel(channel: ChatEntity) {
        router.navigateToEditChannel(channel)
    }

    override fun onUnblockUserClick(member: MemberEntity) {
        viewModelScope.launch {
            chatUseCase.unblockUser(member.id).let {
                if (it.isSuccessful) {
                    userUseCase.update(chatWithUser.user.apply { blockedUsersCount -= 1 })
                    chatUseCase.removeBlockedUser(member.toBlockedUser())
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

    override fun previewAttachment(attachment: MessageAttachment) {
        router.navigateToPreview(
            listOf(
                Attachment(
                    0L,
                    attachment.contentType ?: return,
                    attachment.url,
                    "",
                    Size()
                )
            ),
            0
        )
    }

    override fun onDeleteChat() {
        viewModelScope.launch {
            chatUseCase.delete(chatWithUser.chat.id).let {
                if (it.isSuccessful.not()) {
                    showDefaultErrorMessage(it.errorModel!!.toErrorMessage())
                } else {
                    userUseCase.updateCurrentUserData()
                    router.onBack()
                }
            }
        }
    }

    override fun onReportChatOwner(content: String) {
        viewModelScope.launch {
            userUseCase.report(content, chatWithUser.chat.chatOwnerId.toString()).let {
                if (it.isSuccessful.not()) {
                    showDefaultErrorMessage(it.errorModel!!.toErrorMessage())
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
            chatUseCase.leave(chatWithUser.chat.id).let {
                if (it.isSuccessful.not()) {
                    showDefaultErrorMessage(it.errorModel!!.toErrorMessage())
                } else {
                    router.onBack()
                }
            }
        }
    }

    override fun markMessageAsRead(message: MessageEntity) {
        if (message.userId != chatWithUser.user.id && message.status != MessageStatus.READ && message.isMarkAsReadSentToApprove.not()) {
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
                    else -> Unit
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