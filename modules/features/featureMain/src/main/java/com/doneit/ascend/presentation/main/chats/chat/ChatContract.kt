package com.doneit.ascend.presentation.main.chats.chat

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface ChatContract {
    interface ViewModel : BaseViewModel {
        val user: LiveData<UserEntity>
        val messages: LiveData<PagedList<MessageEntity>>
        val members: LiveData<PagedList<MemberEntity>>

        fun applyData(chat: ChatEntity)
        fun onBackPressed()
    }

    interface Router {
        fun onBack()
    }
}