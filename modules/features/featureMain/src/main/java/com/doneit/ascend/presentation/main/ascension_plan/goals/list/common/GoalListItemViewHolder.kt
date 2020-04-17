package com.doneit.ascend.presentation.main.ascension_plan.goals.list.common

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.marginBottom
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.RepeatType
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.SpiritualActionStepEntity
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.TimeCommitmentType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.ascension_plan.spiritual_action_steps.list.common.SpiritualActionStepViewHolder
import com.doneit.ascend.presentation.main.databinding.GoalChildActionListItemBinding
import java.text.SimpleDateFormat
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
                    SimpleDateFormat("dd MM yyyy", Locale.ENGLISH).format(Date(item.deadline))
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