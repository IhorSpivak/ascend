package com.doneit.ascend.presentation.main.create_group.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.LifecycleViewHolder
import com.doneit.ascend.presentation.main.databinding.TemplateParticipantItemBinding

class ParticipantViewHolder(
    private val binding: TemplateParticipantItemBinding
) : LifecycleViewHolder(binding.root) {

    fun bind(item: String) {

        binding.text = item
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): ParticipantViewHolder {
            val binding: TemplateParticipantItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_participant_item,
                parent,
                false
            )

            return ParticipantViewHolder(binding)
        }
    }
}