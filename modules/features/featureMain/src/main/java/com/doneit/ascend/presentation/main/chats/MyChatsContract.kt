package com.doneit.ascend.presentation.main.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.chat.ChatsWithUser

interface MyChatsContract {
    interface ViewModel : BaseViewModel {
        val chatsWithCurrentUser: MediatorLiveData<ChatsWithUser>
        val chats: LiveData<PagedList<ChatEntity>>
        val filterTextAll: MutableLiveData<String>
        val user: LiveData<UserEntity?>
        fun onBackPressed()
        fun onNewChatPressed()
        fun onNewChannelPressed()
        fun onChatPressed(chat: ChatEntity)
        fun onDelete(chatId: Long)
    }

    interface Router {
        fun onBack()
        fun navigateToChat(id: Long)
        fun navigateToNewChat()
        fun navigateToNewChannel()
    }
}