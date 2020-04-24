package com.doneit.ascend.presentation.main.chats.chat.common

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.common.invisible
import com.doneit.ascend.presentation.main.databinding.ListItemMessageBinding
import com.doneit.ascend.presentation.utils.extensions.*

class MessageViewHolder(
    private val binding: ListItemMessageBinding,
    private val onDeleteClick: (message: MessageEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(
        messageEntity: MessageEntity,
        member: MemberEntity,
        user: UserEntity,
        nextMessage: MessageEntity?
    ) {
        binding.apply {
            this.memberEntity = member
            this.messageEntity = messageEntity
            this.user = user
            ibDelete.setOnClickListener {
                onDeleteClick.invoke(messageEntity)
            }
            if (DateFormat.is24HourFormat(root.context)) {
                this.sendTime = HOUR_24_ONLY_FORMAT.format(messageEntity.createdAt!!)
            } else {
                this.sendTime = HOUR_12_ONLY_FORMAT.format(messageEntity.createdAt!!)
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

    companion object {
        fun create(
            parent: ViewGroup,
            onDeleteClick: (message: MessageEntity) -> Unit
        ): MessageViewHolder {
            val binding: ListItemMessageBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_message,
                parent,
                false
            )
            return MessageViewHolder(binding, onDeleteClick)
        }
    }
}