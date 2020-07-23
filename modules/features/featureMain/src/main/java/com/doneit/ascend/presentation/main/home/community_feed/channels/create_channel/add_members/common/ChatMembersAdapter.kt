package com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel.add_members.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel.add_members.AddMembersContract

class ChatMembersAdapter(
    private val onAdd: (member: AttendeeEntity) -> Unit,
    private val onRemove: (member: AttendeeEntity) -> Unit,
    val model: AddMembersContract.ViewModel
) : PagedListAdapter<AttendeeEntity, ChatMemberViewHolder>(AttendeeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatMemberViewHolder {
        return ChatMemberViewHolder.create(parent, onAdd, onRemove, model)
    }

    override fun onBindViewHolder(holder: ChatMemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class AttendeeDiffCallback : DiffUtil.ItemCallback<AttendeeEntity>() {
        override fun areItemsTheSame(oldItem: AttendeeEntity, newItem: AttendeeEntity): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: AttendeeEntity, newItem: AttendeeEntity): Boolean {
            return newItem.equals(oldItem)
        }

    }
}