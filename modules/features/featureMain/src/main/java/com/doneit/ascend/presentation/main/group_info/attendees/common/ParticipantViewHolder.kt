package com.doneit.ascend.presentation.main.group_info.attendees.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ParticipantItemBinding

class ParticipantViewHolder(
    val binding: ParticipantItemBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(participant: ParticipantEntity){
        binding.apply {
            user = participant
        }
    }
    companion object {
        fun create(
            parent: ViewGroup
        ): ParticipantViewHolder {
            val binding: ParticipantItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.participant_item,
                parent,
                false
            )
            return ParticipantViewHolder(binding)
        }
    }
}