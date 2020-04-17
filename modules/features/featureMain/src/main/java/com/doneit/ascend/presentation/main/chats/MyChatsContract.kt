package com.doneit.ascend.presentation.main.chats

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface MyChatsContract {
    interface ViewModel : BaseViewModel {
        val chats: LiveData<PagedList<ChatEntity>>

        fun onBackPressed()
        fun onNewChatPressed()
        fun onChatPressed(chatId: Long)
        fun onDelete(chatId: Long)
    }

    interface Router {
        fun onBack()
        fun navigateToChat(chatId: Long)
        fun navigateToNewChat()
    }
}