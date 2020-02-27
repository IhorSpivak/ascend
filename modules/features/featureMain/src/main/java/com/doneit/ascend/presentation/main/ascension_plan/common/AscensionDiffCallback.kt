package com.doneit.ascend.presentation.main.ascension_plan.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.ascension.AscensionEntity

class AscensionDiffCallback : DiffUtil.ItemCallback<AscensionEntity>() {
    override fun areItemsTheSame(oldItem: AscensionEntity, newItem: AscensionEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AscensionEntity, newItem: AscensionEntity): Boolean {
        return oldItem == newItem
    }
}