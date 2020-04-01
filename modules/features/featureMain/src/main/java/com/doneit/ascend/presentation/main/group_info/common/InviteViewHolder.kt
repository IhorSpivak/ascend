package com.doneit.ascend.presentation.main.group_info.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.ParticipantEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.InviteListItemBinding

class InviteViewHolder(
    val binding: InviteListItemBinding
): RecyclerView.ViewHolder(binding.root) {
    fun bind(participant: ParticipantEntity){
        binding.apply {
            this.participant = participant
        }
    }
    companion object {
        fun create(
            parent: ViewGroup
        ): InviteViewHolder {
            val binding: InviteListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.invite_list_item,
                parent,
                false
            )
            return InviteViewHolder(
                binding
            )
        }
    }
}