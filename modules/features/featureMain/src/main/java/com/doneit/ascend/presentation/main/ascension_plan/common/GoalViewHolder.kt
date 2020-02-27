package com.doneit.ascend.presentation.main.ascension_plan.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.ascension.GoalEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemGoalBinding
import com.doneit.ascend.presentation.utils.extensions.toDayMonthYear

class GoalViewHolder(
    private val binding: ListItemGoalBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GoalEntity) {
        binding.apply {
            title.text = item.title
            duration.text = item.duration.toString()
            date.text = item.date.toDayMonthYear()
            goal.text = item.goal
        }
    }

    companion object {
        fun create(parent: ViewGroup): GoalViewHolder {
            val binding: ListItemGoalBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_goal,
                parent,
                false
            )

            return GoalViewHolder(binding)
        }
    }
}