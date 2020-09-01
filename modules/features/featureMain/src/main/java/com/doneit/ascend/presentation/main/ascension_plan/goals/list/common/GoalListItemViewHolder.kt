package com.doneit.ascend.presentation.main.ascension_plan.goals.list.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.RepeatType
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.SpiritualActionStepEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.GoalChildActionListItemBinding
import com.doneit.ascend.presentation.utils.extensions.toDayShortMonthYear
import java.util.*

class GoalListItemViewHolder(
    private val binding: GoalChildActionListItemBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: SpiritualActionStepEntity) {
        binding.apply {
            textViewSpiritualActionId.text = (position + 1).toString()
            item.name?.let { nameResId ->
                textViewSpiritualActionName.text = textViewSpiritualActionName.context.getString(nameResId)
            }
            item.repeatType.apply {
                textViewSpiritualActionReiteration.context.let { context ->
                    when (this) {
                        RepeatType.DAY -> textViewSpiritualActionReiteration.text = context.getString(R.string.value_times_per_day, (item.timesCount?.value ?: 0).toString())
                        RepeatType.WEEK -> textViewSpiritualActionReiteration.text = item.weekList?.value?.let {
                            it.joinToString(separator = " | ") { it.toString().subSequence(0, 2) }
                        }
                        RepeatType.MONTH -> textViewSpiritualActionReiteration.text = (item.monthRange?.value ?: 0).toString()
                    }
                }
            }
            item.isCompleted.apply {
                textViewSpiritualActionDone.text = if (this) {
                    textViewSpiritualActionDone.context.getString(R.string.completed)
                } else {
                    Date(item.deadline).toDayShortMonthYear()
                }
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup
        ): GoalListItemViewHolder {
            val binding: GoalChildActionListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.goal_child_action_list_item,
                parent,
                false
            )
            return GoalListItemViewHolder(binding)
        }
    }
}