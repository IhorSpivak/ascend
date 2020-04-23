package com.doneit.ascend.presentation.main.chats.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.chats.new_chat.add_members.AddMemberContract
import com.doneit.ascend.presentation.models.chat.ChatWithUser

interface ChatContract {
    interface ViewModel : BaseViewModel, AddMemberContract.ViewModel {
        val user: LiveData<UserEntity?>
        val messages: LiveData<PagedList<MessageEntity>>
        val chatModel: LiveData<ChatEntity>
        val chatName: LiveData<String>
        val chat: MediatorLiveData<ChatWithUser>

        fun applyData(chat: ChatEntity?, user: UserEntity? = null)
        fun setChat(chat: ChatEntity)
        fun initMessageStream()
        fun updateChatName(newName: String)
        fun sendMessage(message: String)
        fun inviteUser()
        fun onBlockUserClick(userId: Long)
        fun onUnblockUserClick(userId: Long)
        fun onDelete(message: MessageEntity)
    }

    interface Router {
        fun onBack()
    }

    interface LocalRouter {
        fun onBack()
        fun navigateToInviteChatMember()
    }
}