package com.doneit.ascend.presentation.main.chats.chat

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.MessageSocketEntity
import com.doneit.ascend.domain.entity.chats.ChatEntity
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

    override val chatName: MutableLiveData<String> = MutableLiveData()
    override val chat: MediatorLiveData<ChatWithUser> = MediatorLiveData()
    override val chatModel: MutableLiveData<ChatEntity> = MutableLiveData()

    private val invitedMembers: MutableLiveData<List<AttendeeEntity>> = MutableLiveData()
    private val searchQuery = MutableLiveData<String>()
    private var chatMembers: List<Long> = listOf()

    override val canAddMember: Boolean
        get() = selectedMembers.size < 50
    override val selectedMembers: MutableList<AttendeeEntity> = mutableListOf()

    override val searchResult: LiveData<PagedList<AttendeeEntity>>
        get() = searchQuery.switchMap {
            groupUseCase.searchMembers(it, user.value!!.id)
        }
    override val addedMembers: LiveData<List<AttendeeEntity>> = invitedMembers.switchMap {
        liveData {
            chatMembers = it.map { it.id }
            emit(it.toList())
        }
    }

    private val socketMessage = chatUseCase.messagesStream
    private lateinit var observer: Observer<MessageSocketEntity?>
    override val user = userUseCase.getUserLive()
    override val messages: LiveData<PagedList<MessageEntity>> = chatModel.switchMap {
        chatUseCase.getMessageList(
            it.id, MessageListDTO(
                perPage = 10,
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
        viewModelScope.launch {
            val response = chatUseCase.updateChat(chat.value!!.chat.id, chatMembers = chatMembers)
            if (response.isSuccessful) {
                chatName.postValue(response.successModel?.title)
            }
        }
        localRouter.onBack()
    }

    override fun onAddMember(member: AttendeeEntity) {
        selectedMembers.add(member)
        invitedMembers.postValue(selectedMembers)
    }

    override fun onRemoveMember(member: AttendeeEntity) {
        selectedMembers.firstOrNull { it.id == member.id }?.let {
            selectedMembers.remove(it)
            invitedMembers.postValue(selectedMembers)
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
                chatName.postValue(response.successModel?.title)
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

    override fun onBlockUserClick(userId: Long) {
        viewModelScope.launch {
            chatUseCase.blockUser(userId).let {
                if (it.isSuccessful){
                    router.onBack()
                }
            }
        }
    }
    override fun onUnblockUserClick(userId: Long) {
        viewModelScope.launch {
            chatUseCase.unblockUser(userId).let {
                if (it.isSuccessful){
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
}