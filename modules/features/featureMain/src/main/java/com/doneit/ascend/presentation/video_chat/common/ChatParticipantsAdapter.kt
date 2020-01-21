package com.doneit.ascend.presentation.video_chat.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.ImageEntity
import com.doneit.ascend.domain.entity.SocketEvent
import com.doneit.ascend.domain.entity.SocketEventEntity
import com.doneit.ascend.domain.entity.ThumbnailEntity

class ChatParticipantsAdapter(
    private val onItemClick: (Long) -> Unit
) : RecyclerView.Adapter<ChatParticipantViewHolder>() {

    private val items = mutableListOf<SocketEventEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatParticipantViewHolder {
        return ChatParticipantViewHolder.create(parent)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ChatParticipantViewHolder, position: Int) {
        holder.bind(items[position])
        holder.itemView.setOnClickListener {
            onItemClick.invoke(items[position].userId)
        }
    }

    fun submitList(newItems: List<SocketEventEntity>) {
        val diff = DiffUtil.calculateDiff(
            ChatParticipants(
                items,
                newItems
            )
        )

        items.clear()
        items.addAll(newItems)

        diff.dispatchUpdatesTo(this)
    }
}
