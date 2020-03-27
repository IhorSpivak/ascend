package com.doneit.ascend.presentation.main.group_info.attendees.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.doneit.ascend.domain.entity.ParticipantEntity

class ParticipantsAdapter: ListAdapter<ParticipantEntity, ParticipantViewHolder>(DiffParticipant()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffParticipant: DiffUtil.ItemCallback<ParticipantEntity>(){
        override fun areItemsTheSame(
            oldItem: ParticipantEntity,
            newItem: ParticipantEntity
        ): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(
            oldItem: ParticipantEntity,
            newItem: ParticipantEntity
        ): Boolean {
            return oldItem == newItem
        }

    }
}