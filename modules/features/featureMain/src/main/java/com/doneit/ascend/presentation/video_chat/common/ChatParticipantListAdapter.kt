package com.doneit.ascend.presentation.video_chat.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.doneit.ascend.presentation.models.group.PresentationChatParticipant

class ChatParticipantListAdapter(
    private val onItemClick: (String) -> Unit
): ListAdapter<PresentationChatParticipant, ChatParticipantViewHolder>(PresentationChatParticipantDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatParticipantViewHolder {
        return ChatParticipantViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ChatParticipantViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onItemClick.invoke(getItem(position).userId)
        }
    }

    override fun onViewRecycled(holder: ChatParticipantViewHolder) {
        holder.clear()
        super.onViewRecycled(holder)
    }

    private class PresentationChatParticipantDiffCallback: DiffUtil.ItemCallback<PresentationChatParticipant>(){
        override fun areItemsTheSame(
            oldItem: PresentationChatParticipant,
            newItem: PresentationChatParticipant
        ): Boolean {
            return newItem.userId == oldItem.userId
        }

        override fun areContentsTheSame(
            oldItem: PresentationChatParticipant,
            newItem: PresentationChatParticipant
        ): Boolean {
            return newItem == oldItem
        }

    }
}