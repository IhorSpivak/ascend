package com.doneit.ascend.presentation.main.chats.chat

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.MessageSocketEntity
import com.doneit.ascend.domain.entity.chats.BlockedUserEntity
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.dto.MessageDTO
import com.doneit.ascend.domain.entity.dto.MessageListDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.group.GroupUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.chat.ChatWithUser
import com.doneit.ascend.presentation.models.toEntity
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
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
    override val chatModel: MutableLiveData<ChatEntity> = MutableLiveData()

    private val searchQuery = MutableLiveData<String>()

    override val canAddMember: Boolean
        get() = (selectedMembers.size + chat.value!!.chat.members!!.size) < 50

    override val selectedMembers: MutableList<AttendeeEntity> = mutableListOf()

    override val searchResult: LiveData<PagedList<AttendeeEntity>>
        get() = searchQuery.switchMap {
            if(it.length > 1) {
                groupUseCase.searchMembers(
                    it,
                    user.value!!.id,
                    chat.value!!.chat.members?.map { member -> member.toAttendeeEntity() })
            }else{
                MutableLiveData()
            }
        }

    //don't used
    override val addedMembers: LiveData<List<AttendeeEntity>> = MutableLiveData()

    private val socketMessage = chatUseCase.messagesStream
    private lateinit var observer: Observer<MessageSocketEntity?>
    override val user = userUseCase.getUserLive()
    override val messages: LiveData<PagedList<MessageEntity>> = chatModel.switchMap {
        chatUseCase.getMessageList(
            it.id, MessageListDTO(
                perPage = 10,
                sortColumn = "created_at",
                sortType = SortType.ASC
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
    }

    override fun applyData(chatEntity: ChatEntity?, user: UserEntity?) {

        if (chatEntity != null && user != null) {
            chat.postValue(
                ChatWithUser(
                    chatEntity,
                    user
                )
            )
        }
    }

    override fun setChat(chat: ChatEntity) {
        chatUseCase.connectToChannel(chat.id)
        observer = getMessageObserver(chat.id)
        initMessageStream()
        chatModel.postValue(chat)
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
                    }
                    clearResources()
                    localRouter.onBack()
                } else {
                    clearResources()
                    localRouter.onBack()
                }
            }
        }else{
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
        if (query.length > 1) {
            searchQuery.postValue(query)
        }
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
            chatUseCase.sendMessage(MessageDTO(chatModel.value!!.id, message))?.let {
                if (it.isSuccessful) {
                    chatModel.value?.let {
                        chatModel.postValue(it)
                    }
                }
            }
        }
    }

    override fun onBlockUserClick(member: MemberEntity) {
        viewModelScope.launch {
            chatUseCase.blockUser(member.id).let {
                if (it.isSuccessful){
                    user.value?.let { user ->
                        userUseCase.update(user.apply { blockedUsersCount += 1 })
                        chatUseCase.addBlockedUser(member.toBlockedUser())
                    }
                    router.onBack()
                }
            }
        }
    }

    override fun onUnblockUserClick(member: MemberEntity) {
        viewModelScope.launch {
            chatUseCase.unblockUser(member.id).let {
                if (it.isSuccessful){
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
                if (it.isSuccessful){
                    chatUseCase.removeMessageLocal(message)
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

    override fun onCleared() {
        socketMessage.removeObserver(observer)
        chatUseCase.disconnect()
        super.onCleared()
    }

    private fun getMessageObserver(chatId: Long): Observer<MessageSocketEntity?> {
        return Observer { socketEvent ->
            val message = socketEvent?.toEntity()
            message?.let {
                chatUseCase.insertMessage(message, chatId)
            }
        }
    }

    private fun clearResources(){
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