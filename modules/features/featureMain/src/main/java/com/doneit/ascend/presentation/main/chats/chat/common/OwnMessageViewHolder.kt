package com.doneit.ascend.presentation.main.chats.chat.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemOwnMessageBinding
import com.doneit.ascend.presentation.utils.extensions.MESSAGE_FORMATTER
import com.doneit.ascend.presentation.utils.extensions.calculateDate
import com.doneit.ascend.presentation.utils.extensions.toDefaultFormatter
import com.doneit.ascend.presentation.utils.extensions.visibleOrGone

class OwnMessageViewHolder(
    itemView: View,
    private val onDeleteClick: (message: MessageEntity) -> Unit
) : BaseMessageHolder(itemView) {

    override fun bind(
        messageEntity: MessageEntity,
        nextMessage: MessageEntity?,
        memberEntity: MemberEntity,
        chatOwner: MemberEntity,
        currentUserId: Long
    ) {
        DataBindingUtil.bind<ListItemOwnMessageBinding>(itemView)?.apply {
            this.messageEntity = messageEntity
            ibDelete.setOnClickListener {
                onDeleteClick(messageEntity)
            }
            time.apply {
                text = MESSAGE_FORMATTER.toDefaultFormatter().format(messageEntity.createdAt!!)
                visibleOrGone(nextMessage == null || calculateDate(
                    messageEntity.createdAt!!,
                    nextMessage.createdAt!!
                ))
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onDeleteClick: (message: MessageEntity) -> Unit
        ): OwnMessageViewHolder {
            return OwnMessageViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_item_own_message, parent, false),
                onDeleteClick
            )
        }
    }
}