package com.doneit.ascend.presentation.main.ascension_plan.create_spiritual.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.spiritual.SpiritualStepEntity

class SpiritualStepsAdapter(
) : RecyclerView.Adapter<SpiritualStepViewHolder>() {

    private val items = mutableListOf<SpiritualStepEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpiritualStepViewHolder {
        return SpiritualStepViewHolder.create(parent)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: SpiritualStepViewHolder, position: Int) {
        holder.bind(items[position])
    }

    fun submitList(newItems: List<SpiritualStepEntity>) {
        val diff = DiffUtil.calculateDiff(
            SpiritualStepsDiffCallback(
                items,
                newItems
            )
        )

        items.clear()
        items.addAll(newItems)

        diff.dispatchUpdatesTo(this)
    }
}