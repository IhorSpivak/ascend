package com.doneit.ascend.presentation.main.spiritual_action_steps.list.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.SpiritualActionStepEntity

class SpiritualActionListAdapter(
    private val onComplete: (item: SpiritualActionStepEntity) -> Unit,
    private val onEdit: (item: SpiritualActionStepEntity) -> Unit
): PagedListAdapter<SpiritualActionStepEntity, SpiritualActionStepViewHolder>(SpiritualActionStepEntityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): SpiritualActionStepViewHolder {
        return SpiritualActionStepViewHolder.create(parent, onComplete, onEdit)
    }

    override fun onBindViewHolder(holder: SpiritualActionStepViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    private class SpiritualActionStepEntityDiffCallback: DiffUtil.ItemCallback<SpiritualActionStepEntity>(){
        override fun areItemsTheSame(oldItem: SpiritualActionStepEntity, newItem: SpiritualActionStepEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: SpiritualActionStepEntity, newItem: SpiritualActionStepEntity): Boolean {
            return oldItem.equals(newItem)
        }
    }
}