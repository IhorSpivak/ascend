package com.doneit.ascend.presentation.main.create_group.add_member.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.group_info.attendees.common.AttendeeItemViewHolder

class MemberListAdapter(
    private val onDeleteMember: (AttendeeEntity) -> Unit
): ListAdapter<AttendeeEntity, AttendeeItemViewHolder>(AttendeeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendeeItemViewHolder {
        return AttendeeItemViewHolder.create(parent, onDeleteMember)
    }

    override fun onBindViewHolder(holder: AttendeeItemViewHolder, position: Int) {
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