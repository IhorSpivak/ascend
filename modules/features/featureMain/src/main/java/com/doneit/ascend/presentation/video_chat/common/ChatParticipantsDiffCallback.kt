package com.doneit.ascend.presentation.video_chat.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.presentation.models.group.PresentationChatParticipant

class ChatParticipantsDiffCallback(
    private val oldItems: List<PresentationChatParticipant>,
    private val newItems: List<PresentationChatParticipant>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].userId == newItems[newItemPosition].userId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return oldItem == newItem
    }

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

}