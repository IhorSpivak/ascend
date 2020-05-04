package com.doneit.ascend.presentation.main.chats

import androidx.lifecycle.*
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.dto.ChatListDTO
import com.doneit.ascend.domain.entity.dto.SortType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.interactor.chats.ChatUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.chat.ChatsWithUser
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch


@CreateFactory
@ViewModelDiModule
class MyChatsViewModel(
    private val router: MyChatsContract.Router,
    private val userUseCase: UserUseCase,
    private val chatUseCase: ChatUseCase
) : BaseViewModelImpl(), MyChatsContract.ViewModel {
    override lateinit var chats: LiveData<PagedList<ChatEntity>>
    val user = userUseCase.getUserLive()
    override val filterTextAll: MutableLiveData<String> = MutableLiveData("")
    override val chatsWithCurrentUser: MediatorLiveData<ChatsWithUser> = MediatorLiveData()
    init {
        chats = Transformations.switchMap(filterTextAll) { query ->
            val model = ChatListDTO(
                perPage = 10,
                sortColumn = "last_message",
                sortType = SortType.DESC,
                title = query
            )
            return@switchMap chatUseCase.getMyChatListLive(model)
        }

        chatsWithCurrentUser.addSource(user) {
            applyData(chats.value, it)
        }

        chatsWithCurrentUser.addSource(chats) {
            applyData(it, user.value)
        }
    }

    private fun applyData(chatEntity: PagedList<ChatEntity>?, user: UserEntity?) {

        if (chatEntity != null && user != null) {
            chatsWithCurrentUser.postValue(
                ChatsWithUser(
                    chatEntity,
                    user
                )
            )
        }
    }

    override fun onBackPressed() {
        router.onBack()
    }

    override fun onNewChatPressed() {
        router.navigateToNewChat()
    }

    override fun onChatPressed(chat: ChatEntity) {
        router.navigateToChat(chat)
    }

    override fun onDelete(chatId: Long) {
        viewModelScope.launch {
            val response = chatUseCase.delete(chatId)

            if (response.isSuccessful.not()) {
                showDefaultErrorMessage(response.errorModel!!.toErrorMessage())
            }
        }
    }
}