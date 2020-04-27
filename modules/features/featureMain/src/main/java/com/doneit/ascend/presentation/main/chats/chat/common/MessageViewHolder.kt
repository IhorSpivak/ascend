package com.doneit.ascend.presentation.main.chats.chat.common

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.chats.MessageType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.common.invisible
import com.doneit.ascend.presentation.main.common.visible
import com.doneit.ascend.presentation.main.databinding.ListItemMessageBinding
import com.doneit.ascend.presentation.utils.extensions.START_TIME_FORMATTER
import com.doneit.ascend.presentation.utils.extensions.TIME_12_FORMAT_DROP_DAY
import com.doneit.ascend.presentation.utils.extensions.TIME_24_FORMAT_DROP_DAY
import com.doneit.ascend.presentation.utils.extensions.calculateDate

class MessageViewHolder(
    private val binding: ListItemMessageBinding,
    private val onDeleteClick: (message: MessageEntity) -> Unit,
    private val onMenuClick: (view: View, id: Long) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    fun bind(
        messageEntity: MessageEntity,
        member: MemberEntity,
        user: UserEntity,
        nextMessage: MessageEntity?,
        chat: ChatEntity
    ) {
        when(messageEntity.type){
            MessageType.INVITE -> {
                setSystemMessage(binding.root.context.resources.getString(R.string.invite_message, user.displayName, member.fullName))
            }
            MessageType.LEAVE -> {
                setSystemMessage(binding.root.context.resources.getString(R.string.leave_message, member.fullName))
            }
            else -> {

                binding.apply {
                    if (messageEntity.id == user.id) {
                        itemLayout.gone()
                        memberMessageContainer.visible()
                    } else {
                        itemLayout.visible()
                        memberMessageContainer.invisible()
                    }
                    this.memberEntity = member
                    this.messageEntity = messageEntity
                    this.user = user
                    ibDelete.setOnClickListener {
                        onDeleteClick.invoke(messageEntity)
                    }
                    userImage.setOnLongClickListener {
                        if (chat.membersCount > 2) {
                            if (chat.chatOwnerId == user.id) {
                                onMenuClick(it, member.id)
                                true
                            }else{
                                false
                            }
                        } else {
                            false
                        }
                    }
                    if(DateFormat.is24HourFormat(root.context)){
                        this.sendTime = TIME_24_FORMAT_DROP_DAY.format(messageEntity.createdAt!!)
                    }else{
                        this.sendTime = TIME_12_FORMAT_DROP_DAY.format(messageEntity.createdAt!!)
                    }
                    if (nextMessage == null) {
                        time.text = START_TIME_FORMATTER.format(messageEntity.createdAt!!)
                        time.visible()
                        corner.visible()
                        userImage.visible()
                        isOnline.visible()
                    } else {
                        time.apply {
                            text = START_TIME_FORMATTER.format(messageEntity.createdAt!!)
                            corner.apply {
                                if (messageEntity.userId == nextMessage.userId) {
                                    userImage.invisible()
                                    isOnline.gone()
                                    this.gone()
                                } else {
                                    userImage.visible()
                                    isOnline.visible()
                                    this.visible()
                                }
                            }
                            if (calculateDate(messageEntity.createdAt!!, nextMessage.createdAt!!)) {
                                this.visible()
                            } else {
                                this.gone()
                            }
                        }
                    }
                }
            }
        }

    }
    private fun setSystemMessage(message: String){
        binding.apply {
            itemLayout.gone()
            memberMessageContainer.gone()
            time.text = message
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onDeleteClick: (message: MessageEntity) -> Unit,
            onMenuClick: (view: View, id: Long) -> Unit
        ): MessageViewHolder {
            val binding: ListItemMessageBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_message,
                parent,
                false
            )
            return MessageViewHolder(binding, onDeleteClick, onMenuClick)
        }
    }
}