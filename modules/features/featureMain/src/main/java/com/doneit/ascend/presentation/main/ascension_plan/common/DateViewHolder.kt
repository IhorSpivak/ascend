package com.doneit.ascend.presentation.main.ascension_plan.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.ascension.AscensionEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemAscensionDateBinding
import com.doneit.ascend.presentation.utils.extensions.toDayMonthYear

class DateViewHolder(
    private val binding: ListItemAscensionDateBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: AscensionEntity) {
        binding.tvDate.text = item.date.toDayMonthYear()
    }

    companion object {
        fun create(parent: ViewGroup): DateViewHolder {
            val binding: ListItemAscensionDateBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_ascension_date,
                parent,
                false
            )

            return DateViewHolder(binding)
        }
    }
}