package com.doneit.ascend.presentation.main.create_group.add_member.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.group_info.attendees.AttendeesContract

class MemberAdapter(
    private val onAdd: (member: AttendeeEntity) -> Unit,
    private val onRemove: (member: AttendeeEntity) -> Unit,
    val model: AddMemberViewModel
): PagedListAdapter<AttendeeEntity, MemberViewHolder>(AttendeeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        return MemberViewHolder.create(parent, onAdd, onRemove, model)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class AttendeeDiffCallback: DiffUtil.ItemCallback<AttendeeEntity>(){
        override fun areItemsTheSame(oldItem: AttendeeEntity, newItem: AttendeeEntity): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: AttendeeEntity, newItem: AttendeeEntity): Boolean {
            return newItem.equals(oldItem)
        }

    }
}