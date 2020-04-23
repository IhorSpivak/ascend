package com.doneit.ascend.presentation.main.chats.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.AttendeeEntity

class AttendeeDiffCallback: DiffUtil.ItemCallback<AttendeeEntity>(){
    override fun areItemsTheSame(oldItem: AttendeeEntity, newItem: AttendeeEntity): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: AttendeeEntity, newItem: AttendeeEntity): Boolean {
        return newItem.equals(oldItem)
    }
}