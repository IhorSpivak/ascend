package com.doneit.ascend.presentation.main.create_group.master_mind.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.doneit.ascend.domain.entity.AttendeeEntity

class InvitedMembersAdapter(
    private val onDeleteMember: (AttendeeEntity) -> Unit
): ListAdapter<AttendeeEntity, AttendeeViewHolder>(AttendeeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendeeViewHolder {
        return AttendeeViewHolder.create(parent, onDeleteMember)
    }

    override fun onBindViewHolder(holder: AttendeeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class AttendeeDiffCallback: DiffUtil.ItemCallback<AttendeeEntity>(){
        override fun areItemsTheSame(oldItem: AttendeeEntity, newItem: AttendeeEntity): Boolean {
            return newItem == oldItem
        }

        override fun areContentsTheSame(oldItem: AttendeeEntity, newItem: AttendeeEntity): Boolean {
            return newItem.id == oldItem.id
        }

    }
}