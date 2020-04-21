package com.doneit.ascend.presentation.main.chats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface MyChatsContract {
    interface ViewModel : BaseViewModel {
        val chats: LiveData<PagedList<ChatEntity>>
        val filterTextAll: MutableLiveData<String>
        fun onBackPressed()
        fun onNewChatPressed()
        fun onChatPressed(chat: ChatEntity)
        fun onDelete(chatId: Long)
    }

    interface Router {
        fun onBack()
        fun navigateToChat(chat: ChatEntity)
        fun navigateToNewChat()
    }
}