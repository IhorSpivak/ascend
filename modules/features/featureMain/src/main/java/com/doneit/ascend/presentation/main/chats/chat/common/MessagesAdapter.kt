package com.doneit.ascend.presentation.main.chats.chat.common

import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.doneit.ascend.domain.entity.MessageAttachment
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.chats.MessageType
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.chats.chat.ChatFragment
import com.doneit.ascend.presentation.main.chats.chat.holder.*
import com.doneit.ascend.presentation.models.chat.ChatWithUser

class MessagesAdapter(
    private val chatWithUser: ChatWithUser,
    private val onButtonClick: (message: MessageEntity) -> Unit,
    private val onImageLongClick: (v: View, id: Long) -> Unit,
    private val onImageClick: (view: View, id: Long) -> Unit,
    private val onImageWebinarClick: (view: View, member: MemberEntity) -> Unit,
    private val previewAttachment: (MessageAttachment) -> Unit,
    private val onSeeMoreClick: (Post) -> Unit,
    private val onSharedProfileClick: (UserEntity) -> Unit,
    private val onSharedGroupClick: (GroupEntity) -> Unit,
    private val cancelUpload: (MessageAttachment) -> Unit
) : PagedListAdapter<MessageEntity, BaseMessageHolder>(MessageDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseMessageHolder {
        return when (viewType) {
            Type.OWN.ordinal -> if (chatWithUser.chatType != ChatType.WEBINAR_CHAT) OwnMessageViewHolder.create(
                parent,
                onButtonClick
            ) else WebinarMessageViewHolder.create(
                parent,
                onButtonClick,
                { view: View, l: Long -> },
                onImageWebinarClick
            )
            Type.SYSTEM.ordinal -> SystemMessageViewHolder.create(
                parent
            )
            Type.OTHER.ordinal -> if (chatWithUser.chatType != ChatType.WEBINAR_CHAT) OtherMessageViewHolder.create(
                parent,
                { view: View, id: Long ->
                    with(chatWithUser) {
                        if (chat.membersCount == ChatFragment.PRIVATE_CHAT_MEMBER_COUNT && chat.chatOwnerId == user.id) {
                            onImageLongClick.invoke(view, id)
                        }
                    }
                },
                onImageClick,
                onImageWebinarClick,
                chatWithUser.chatType
            ) else WebinarMessageViewHolder.create(
                parent,
                onButtonClick,
                { view: View, l: Long -> },
                onImageWebinarClick
            )
            Type.SHARE_POST.ordinal -> ShareViewHolder.create(parent, onSeeMoreClick)
            Type.ATTACHMENT_OWN.ordinal -> AttachmentOwnViewHolder.create(
                parent,
                onButtonClick,
                previewAttachment,
                cancelUpload
            )
            Type.ATTACHMENT_OTHER.ordinal -> AttachmentOtherViewHolder.create(
                parent,
                previewAttachment
            )
            Type.SHARE_GROUP.ordinal -> ShareGroupViewHolder.create(parent, onSharedGroupClick)
            Type.SHARE_PROFILE_OWN.ordinal -> ProfileShareOwnViewHolder.create(
                parent,
                onSharedProfileClick
            )
            Type.SHARE_PROFILE_OTHER.ordinal -> ProfileShareOtherViewHolder.create(
                parent,
                onSharedProfileClick
            )
            else -> throw IllegalArgumentException("Unsupported view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: BaseMessageHolder, position: Int) {
        val message = getItem(position)!!
        val nextMessage = if (position != itemCount - 1) getItem(position + 1) else null
        val member = chatWithUser.chat.members.firstOrNull { it.id == message.userId } ?: return

        when (getItemViewType(position)) {
            Type.OWN.ordinal -> {
                if (chatWithUser.chatType != ChatType.WEBINAR_CHAT) {
                    (holder as OwnMessageViewHolder).bind(
                        messageEntity = message,
                        nextMessage = nextMessage
                    )
                } else {
                    (holder as WebinarMessageViewHolder).bind(
                        messageEntity = message,
                        nextMessage = nextMessage,
                        memberEntity = member,
                        currentUserId = chatWithUser.user.id
                    )
                }
            }
            Type.SYSTEM.ordinal -> (holder as SystemMessageViewHolder).bind(
                messageEntity = message,
                memberEntity = member,
                chatOwner = chatWithUser.chat.members.firstOrNull {
                    it.id == chatWithUser.chat.chatOwnerId
                } ?: return,
                currentUserId = chatWithUser.user.id
            )
            Type.OTHER.ordinal -> if (chatWithUser.chatType != ChatType.WEBINAR_CHAT) {
                (holder as OtherMessageViewHolder).bind(
                    messageEntity = message,
                    memberEntity = member,
                    nextMessage = nextMessage
                )
            } else {
                (holder as WebinarMessageViewHolder).bind(
                    messageEntity = message,
                    memberEntity = member,
                    nextMessage = nextMessage,
                    currentUserId = chatWithUser.user.id
                )
            }
            Type.SHARE_POST.ordinal -> (holder as ShareViewHolder).bind(
                memberEntity = member,
                messageEntity = message
            )
            Type.ATTACHMENT_OWN.ordinal -> (holder as AttachmentOwnViewHolder).bind(
                messageEntity = message,
                nextMessage = nextMessage
            )
            Type.ATTACHMENT_OTHER.ordinal -> (holder as AttachmentOtherViewHolder).bind(
                messageEntity = message,
                memberEntity = member,
                nextMessage = nextMessage
            )
            Type.SHARE_PROFILE_OWN.ordinal -> (holder as ProfileShareOwnViewHolder).bind(
                messageEntity = message
            )
            Type.SHARE_PROFILE_OTHER.ordinal -> (holder as ProfileShareOtherViewHolder).bind(
                messageEntity = message,
                memberEntity = member
            )
            Type.SHARE_GROUP.ordinal -> (holder as ShareGroupViewHolder).bind(
                memberEntity = member,
                messageEntity = message
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return convertItemToType(getItem(position)!!).ordinal
    }

    private fun convertItemToType(message: MessageEntity): Type {
        return when (message.type) {
            MessageType.POST_SHARE -> Type.SHARE_POST
            MessageType.ATTACHMENT -> if (message.userId == chatWithUser.user.id) {
                Type.ATTACHMENT_OWN
            } else Type.ATTACHMENT_OTHER
            MessageType.MESSAGE -> if (message.userId == chatWithUser.user.id) {
                Type.OWN
            } else Type.OTHER
            MessageType.INVITE,
            MessageType.USER_REMOVED,
            MessageType.LEAVE -> Type.SYSTEM
            MessageType.GROUP_SHARE -> Type.SHARE_GROUP
            MessageType.PROFILE_SHARE -> if (message.userId == chatWithUser.user.id) {
                Type.SHARE_PROFILE_OWN
            } else Type.SHARE_PROFILE_OTHER
            else -> Type.OTHER
        }
    }

    enum class Type {
        OWN,
        SYSTEM,
        OTHER,
        SHARE_POST,
        ATTACHMENT_OWN,
        ATTACHMENT_OTHER,
        SHARE_PROFILE_OWN,
        SHARE_PROFILE_OTHER,
        SHARE_GROUP
    }
}