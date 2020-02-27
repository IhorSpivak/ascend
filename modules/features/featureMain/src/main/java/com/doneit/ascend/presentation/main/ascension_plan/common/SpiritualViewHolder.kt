package com.doneit.ascend.presentation.main.ascension_plan.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.ascension.SpiritualStepEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemSpiritualBinding
import com.doneit.ascend.presentation.utils.extensions.toDayMonthYear

class SpiritualViewHolder(
    private val binding: ListItemSpiritualBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SpiritualStepEntity) {
        binding.apply {
            title.text = item.title
            duration.text = item.timeCommitment
            date.text = item.date.toDayMonthYear()
        }
    }

    companion object {
        fun create(parent: ViewGroup): SpiritualViewHolder {
            val binding: ListItemSpiritualBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_spiritual,
                parent,
                false
            )

            return SpiritualViewHolder(binding)
        }
    }
}