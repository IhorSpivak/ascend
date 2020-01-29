package com.doneit.ascend.presentation.video_chat.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.domain.entity.SocketUserEntity

class ChatParticipantsDiffCallback(
    private val oldItems: List<SocketUserEntity>,
    private val newItems: List<SocketUserEntity>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].userId == newItems[newItemPosition].userId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size
}