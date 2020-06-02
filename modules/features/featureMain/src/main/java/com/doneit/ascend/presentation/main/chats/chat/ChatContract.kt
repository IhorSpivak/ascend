package com.doneit.ascend.presentation.main.chats.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
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
        val membersCountGroup: LiveData<Int>
        val chat: MediatorLiveData<ChatWithUser>

        fun markMessageAsRead(message: MessageEntity)
        fun applyData(chat: ChatEntity?, user: UserEntity? = null)
        fun setChat(id: Long)
        fun initMessageStream()
        fun updateChatName(newName: String)
        fun sendMessage(message: String)
        fun inviteUser()
        fun onBlockUserClick(member: MemberEntity)
        fun onUnblockUserClick(member: MemberEntity)
        fun showDetailedUser(userId: Long)
        fun onDelete(message: MessageEntity)
        fun onDeleteChat()
        fun onReportChatOwner(content: String)
        fun onReport(content: String, id: Long)
        fun onLeave()
    }

    interface Router {
        fun onBack()
        fun goToDetailedUser(id: Long)
    }

    interface LocalRouter {
        fun onBack()
        fun navigateToInviteChatMember()
    }
}