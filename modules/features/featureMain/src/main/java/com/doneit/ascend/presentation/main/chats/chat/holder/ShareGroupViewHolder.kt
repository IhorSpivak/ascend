package com.doneit.ascend.presentation.main.chats.chat.holder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.common.setOnSingleClickListener
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemSharedGroupBinding
import com.doneit.ascend.presentation.utils.extensions.getTimeFormat

class ShareGroupViewHolder(
    itemView: View,
    private val onSharedGroupClick: (GroupEntity) -> Unit
) : BaseMessageHolder(itemView) {

    private val binding: ListItemSharedGroupBinding = DataBindingUtil.getBinding(itemView)!!

    fun bind(
        messageEntity: MessageEntity,
        memberEntity: MemberEntity
    ) {
        with(binding) {
            member = memberEntity

            this.messageEntity = messageEntity
            messageEntity.sharedGroup?.let { group ->
                groupModel = group
                community = group.community
                if (messageEntity.createdAt != null) {
                    messageTime.text = root.context.getTimeFormat().format(messageEntity.createdAt)
                }
                theme = if (group.pastMeetingsCount == group.meetingsCount) {
                    group.pastMeetingsCount?.let { group.themes?.getOrNull(it - 1) }
                } else {
                    group.pastMeetingsCount?.let { group.themes?.getOrNull(it) }
                }
                itemView.setOnSingleClickListener {
                    onSharedGroupClick(group)
                }
            }

        }


    }

    companion object {
        fun create(
            parent: ViewGroup,
            onSharedGroupClick: (GroupEntity) -> Unit
        ): ShareGroupViewHolder {
            return ShareGroupViewHolder(
                DataBindingUtil.inflate<ListItemSharedGroupBinding>(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_shared_group,
                    parent,
                    false
                ).root,
                onSharedGroupClick
            )
        }
    }
}