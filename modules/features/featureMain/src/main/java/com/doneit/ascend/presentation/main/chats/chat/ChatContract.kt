package com.doneit.ascend.presentation.main.chats.chat

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.MessageAttachment
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.dto.ChatType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.chats.new_chat.add_members.AddMemberContract
import com.doneit.ascend.presentation.models.chat.ChatWithUser

interface ChatContract {
    interface ViewModel : BaseViewModel, AddMemberContract.ViewModel {
        val messages: LiveData<PagedList<MessageEntity>>
        val membersCountGroup: LiveData<Int>
        val chat: LiveData<ChatWithUser>

        fun markMessageAsRead(message: MessageEntity)
        fun initMessageStream()
        fun updateChatName(newName: String)
        fun sendMessage(message: String, attachmentType: String = "", attachmentUrl: String = "")
        fun inviteUser()
        fun onImageClick()
        fun onChatDetailsClick()
        fun onBlockUserClick(member: MemberEntity)
        fun onUnblockUserClick(member: MemberEntity)
        fun showDetailedUser(userId: Long)
        fun showLiveStreamUser(member: MemberEntity)
        fun goToEditChannel(channel: ChatEntity)
        fun onDelete(message: MessageEntity)
        fun previewAttachment(attachment: MessageAttachment)
        fun onDeleteChat()
        fun onReportChatOwner(content: String)
        fun goToUserList(channel: ChatEntity)
        fun showPostDetails(post: Post)
        fun onReport(content: String, id: Long)
        fun onLeave()
    }

    interface Router {
        fun onBack()
        fun navigateToPostDetails(user: UserEntity, post: Post)
        fun navigateToEditChannel(channel: ChatEntity)
        fun navigateToAddChannelMembers()
        fun goToDetailedUser(id: Long)
        fun goToChatMembers(
            chatId: Long,
            chatOwner: Long,
            chatType: ChatType,
            members: List<MemberEntity>,
            user: UserEntity
        )

        fun navigateToPreview(attachments: List<Attachment>, selected: Int)
        fun goToLiveStreamUser(member: MemberEntity)
    }

    interface LocalRouter {
        fun onBack()
        fun navigateToInviteChatMember()
    }
}