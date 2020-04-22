package com.doneit.ascend.presentation.main.chats.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.chat.ChatWithUser

interface ChatContract {
    interface ViewModel : BaseViewModel {
        val user: LiveData<UserEntity?>
        val messages: LiveData<PagedList<MessageEntity>>
        val chatModel: LiveData<ChatEntity>
        val chatName: LiveData<String>
        val chat: MediatorLiveData<ChatWithUser>

        fun applyData(chat: ChatEntity?, user: UserEntity? = null)
        fun setChat(chat: ChatEntity)
        fun initMessageStream()
        fun onBackPressed()
        fun updateChatName(newName: String)
        fun sendMessage(message: String)
    }

    interface Router {
        fun onBack()
    }
}