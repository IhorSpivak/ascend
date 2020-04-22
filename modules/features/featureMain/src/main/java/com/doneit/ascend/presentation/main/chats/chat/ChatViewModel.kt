package com.doneit.ascend.presentation.main.chats.chat

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MessageSocketEntity
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.dto.MessageDTO
import com.doneit.ascend.domain.entity.dto.MessageListDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
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
    private val router: ChatContract.Router
) : BaseViewModelImpl(), ChatContract.ViewModel {

    override val chatName: MutableLiveData<String> = MutableLiveData()
    override val chat: MediatorLiveData<ChatWithUser> = MediatorLiveData()
    override val chatModel: MutableLiveData<ChatEntity> = MutableLiveData()
    private val loadMembersModel: MutableLiveData<ChatEntity> = MutableLiveData()
    private val socketMessage = chatUseCase.messagesStream
    private lateinit var observer: Observer<MessageSocketEntity>
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
        observer = getMessageObserver(chat.id)
        initMessageStream()
        chatUseCase.connectToChannel(chat.id)
        chatModel.postValue(chat)
    }

    override fun initMessageStream() {
        socketMessage.observeForever(observer)
    }

    override fun onBackPressed() {
        router.onBack()
    }

    override fun updateChatName(newName: String) {
        viewModelScope.launch {
            val response = chatUseCase.updateChat(chatModel.value!!.id, title = newName)
            if (response.isSuccessful) {
                chatName.postValue(response.successModel?.title)
            }
        }
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

    override fun onCleared() {
        super.onCleared()
        socketMessage.removeObserver(observer)
    }

    private fun getMessageObserver(chatId: Long): Observer<MessageSocketEntity> {
        return Observer { socketEvent ->
            val message = socketEvent.toEntity()
            chatUseCase.insertMessage(message, chatId)
        }
    }
}