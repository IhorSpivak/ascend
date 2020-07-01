package com.doneit.ascend.presentation.video_chat.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.models.group.PresentationChatParticipant
import com.twilio.video.ChatParticipantViewHolder

class ChatParticipantsAdapter(
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<ChatParticipantViewHolder>() {

    private val items = mutableListOf<PresentationChatParticipant>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatParticipantViewHolder {
        return ChatParticipantViewHolder.create(parent)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ChatParticipantViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            if(!items[holder.adapterPosition].isOwner)
            onItemClick.invoke(items[holder.adapterPosition].userId)
        }
    }

    override fun onViewRecycled(holder: ChatParticipantViewHolder) {
        holder.clear()
    }

    fun submitList(newItems: List<PresentationChatParticipant>) {
        val diff = DiffUtil.calculateDiff(
            ChatParticipantsDiffCallback(
                items,
                newItems
            )
        )

        items.clear()
        items.addAll(newItems)

        diff.dispatchUpdatesTo(this)
    }
}
