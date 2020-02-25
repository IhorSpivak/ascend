package com.doneit.ascend.presentation.main.ascension_plan.create_spiritual.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemSpiritualStepBinding
import com.doneit.ascend.domain.entity.spiritual.SpiritualStepEntity

class SpiritualStepViewHolder(
    private val binding: ListItemSpiritualStepBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(step: SpiritualStepEntity) {
        binding.step = step
    }

    companion object {
        fun create(parent: ViewGroup): SpiritualStepViewHolder {
            val binding: ListItemSpiritualStepBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_spiritual_step,
                parent,
                false
            )

            return SpiritualStepViewHolder(binding)
        }
    }
}