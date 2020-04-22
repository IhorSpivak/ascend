package com.doneit.ascend.presentation.main.chats.chat

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MessageSocketEntity
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.dto.MemberListDTO
import com.doneit.ascend.domain.entity.dto.MessageDTO
import com.doneit.ascend.domain.entity.dto.MessageListDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.toEntity
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class ChatViewModel(
    private val chatUseCase: ChatUseCase,
    private val userUseCase: UserUseCase,
    private val router: ChatContract.Router
) : BaseViewModelImpl(), ChatContract.ViewModel {

    override val chatName: MutableLiveData<String> = MutableLiveData()
    private val chatModel: MutableLiveData<ChatEntity> = MutableLiveData()
    private val loadMembersModel: MutableLiveData<ChatEntity> = MutableLiveData()
    private val socketMessage = chatUseCase.messagesStream
    private lateinit var observer: Observer<MessageSocketEntity>
    override val members: LiveData<PagedList<MemberEntity>> = loadMembersModel.switchMap {
        chatUseCase.getMemberList(
            it.id, MemberListDTO(
                perPage = 50,
                sortColumn = "created_at",
                sortType = SortType.ASC
            )
        )
    }

    override val user: LiveData<UserEntity> = liveData { emit(userUseCase.getUser()!!) }

    override val messages: LiveData<PagedList<MessageEntity>> = chatModel.switchMap {
        chatUseCase.getMessageList(
            it.id, MessageListDTO(
                perPage = 10,
                sortType = SortType.DESC
            )
        )
    }

    override fun applyData(chat: ChatEntity) {
        observer = getMessageObserver(chat.id)
        initMessageStream()
        chatUseCase.connectToChannel(chat.id)
        chatModel.postValue(chat)
    }

    override fun loadMembers(chat: ChatEntity) {
        loadMembersModel.postValue(chat)
    }

    override fun initMessageStream() {
        socketMessage.observeForever(observer)
    }

    override fun onBackPressed() {
        router.onBack()
    }

    override fun updateChatName(chatId: Long, newName: String) {
        viewModelScope.launch {
            val response = chatUseCase.updateChat(chatId, title = newName)
            if (response.isSuccessful) {
                chatName.postValue(response.successModel?.title)
            }
        }
    }

    override fun sendMessage(id: Long, message: String) {
        viewModelScope.launch {
            chatUseCase.sendMessage(MessageDTO(id, message))?.let {
                if (it.isSuccessful) {
                    chatModel.value?.let {
                        chatModel.postValue(it)
                    }
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        socketMessage.removeObserver(observer)
    }

    private fun getMessageObserver(chatId: Long):Observer<MessageSocketEntity>{
        return Observer { socketEvent ->
            val message = socketEvent.toEntity()
            chatUseCase.insertMessage(message, chatId)
        }
    }
}