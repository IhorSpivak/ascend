package com.doneit.ascend.presentation.video_chat.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.UserEntity

class ChatParticipantsAdapter : RecyclerView.Adapter<ChatParticipantViewHolder>() {

    private val items = mutableListOf<UserEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatParticipantViewHolder {
        return ChatParticipantViewHolder.create(parent)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ChatParticipantViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun submitList(newItems: List<UserEntity>) {
        val diff = DiffUtil.calculateDiff(
            UserDiffCallback(
                items,
                newItems
            )
        )

        items.clear()
        items.addAll(newItems)

        diff.dispatchUpdatesTo(this)
    }
}
