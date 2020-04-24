package com.doneit.ascend.presentation.main.chats.chat.common

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemMessageBinding
import com.doneit.ascend.presentation.utils.extensions.HOUR_12_ONLY_FORMAT
import com.doneit.ascend.presentation.utils.extensions.HOUR_24_ONLY_FORMAT
import com.doneit.ascend.presentation.utils.extensions.START_TIME_FORMATTER
import com.doneit.ascend.presentation.utils.extensions.calculateDate

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
                            userImage.gone()
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

    private fun View.visible() {
        this.visibility = View.VISIBLE
    }

    private fun View.gone() {
        this.visibility = View.GONE
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