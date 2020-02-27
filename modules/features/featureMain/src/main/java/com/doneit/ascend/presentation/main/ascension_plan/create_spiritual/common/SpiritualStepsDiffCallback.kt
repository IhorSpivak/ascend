package com.doneit.ascend.presentation.main.ascension_plan.create_spiritual.common

import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.ascension.SpiritualStepEntity

class SpiritualStepsDiffCallback(
    private val oldItems: List<SpiritualStepEntity>,
    private val newItems: List<SpiritualStepEntity>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size
}