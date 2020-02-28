package com.doneit.ascend.presentation.main.ascension_plan.goals.list.common

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.SpiritualActionStepEntity

class GoalListItemValueAdatper : ListAdapter<SpiritualActionStepEntity, GoalListItemViewHolder>(SpiritualActionStepEntityDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoalListItemViewHolder {
        return GoalListItemViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: GoalListItemViewHolder, position: Int) {
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