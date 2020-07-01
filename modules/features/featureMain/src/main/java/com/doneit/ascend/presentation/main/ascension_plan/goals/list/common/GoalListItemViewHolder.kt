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
            textViewSpiritualActionName.text = item.name
            item.repeatType.apply {
                when(this){
                    RepeatType.DAY -> textViewSpiritualActionReiteration.text = item.timesCount!!.value.toString().plus(" times per day")
                    RepeatType.WEEK -> textViewSpiritualActionReiteration.text = item.weekList!!.value.let {
                        it!!.joinToString(separator = " | ") { it.toString().subSequence(0, 2) }
                    }
                    RepeatType.MONTH -> textViewSpiritualActionReiteration.text = item.monthRange!!.value.toString()
                }
            }
            item.isCompleted.apply {
                textViewSpiritualActionDone.text = if (this){
                    "Completed"
                }else{
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