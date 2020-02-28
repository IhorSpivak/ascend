package com.doneit.ascend.presentation.main.ascension_plan.goals.list.common

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.doneit.ascend.domain.entity.ascension.goal.GoalEntity

class GoalsListAdapter (
    private val onComplete: (item: GoalEntity) -> Unit,
    private val onEdit: (item: GoalEntity) -> Unit
): PagedListAdapter<GoalEntity, GoalViewHolder>(SpiritualActionStepEntityDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): GoalViewHolder {
        return GoalViewHolder.create(parent, onComplete, onEdit)
    }

    override fun onBindViewHolder(holder: GoalViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    private class SpiritualActionStepEntityDiffCallback: DiffUtil.ItemCallback<GoalEntity>(){
        override fun areItemsTheSame(oldItem: GoalEntity, newItem: GoalEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: GoalEntity, newItem: GoalEntity): Boolean {
            return oldItem.equals(newItem)
        }
    }
}