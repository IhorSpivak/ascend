package com.doneit.ascend.presentation.main.ascension_plan.goals.list.common

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.ascension.goal.GoalDurationType
import com.doneit.ascend.domain.entity.ascension.goal.GoalEntity
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.RepeatType
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.SpiritualActionStepEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.GoalsListItemBinding
import com.doneit.ascend.presentation.main.databinding.SpiritualActionListItemBinding

class GoalViewHolder(
    private val binding: GoalsListItemBinding,
    private val onComplete: (item: GoalEntity) -> Unit,
    private val onEdit: (item: GoalEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    /*val childBinding: SpiritualActionListItemBinding by lazy {
        DataBindingUtil.inflate<SpiritualActionListItemBinding>(
            LayoutInflater.from(binding.root.context),
            R.layout.spiritual_action_list_item,
            binding.root.parent as ViewGroup,
            false
        )
    }*/

    fun bind(item: GoalEntity) {
        binding.apply {
            textViewGoalId.text = position.toString()
            textViewGoalName.text = item.name
            item.duration.apply {
                when(type){
                    GoalDurationType.DAYS -> textViewGoalCommitmentTime.text = value.toString().plus(" days")
                    GoalDurationType.WEEKS -> textViewGoalCommitmentTime.text = value.toString().plus(" weeks")
                    GoalDurationType.MONTHS -> textViewGoalCommitmentTime.text = value.toString().plus(" months")
                    GoalDurationType.YEARS -> textViewGoalCommitmentTime.text = value.toString().plus(" years")
                }
            }

            buttonGoalInteraction.apply {
                setOnClickListener {
                    PopupMenu(it.context, it, Gravity.START).apply {
                        menuInflater.inflate(R.menu.spiritual_action_steps_popup, this.menu)
                        setOnMenuItemClickListener {
                            when(it.itemId){
                                R.id.spiritual_action_menu_on_complete ->{
                                    onComplete.invoke(item)
                                    true
                                }
                                R.id.spiritual_action_menu_on_edit ->{
                                    onEdit.invoke(item)
                                    true
                                }
                                else -> false
                            }
                        }
                    }.show()
                }
            }

            val actionListAdapter = GoalListItemValueAdatper()
            actionStepListContainer.adapter = actionListAdapter
            actionListAdapter.submitList(item.actionStepList)
        }
    }
    private fun updateList(listContainer: LinearLayout, items: List<SpiritualActionStepEntity>){
        /*listContainer.removeAllViews()
        items.forEach {
           childBinding.apply {
               textViewSpiritualActionName.text = it.name
               it.repeatType.apply {
                   when(this){
                       RepeatType.DAY -> textViewSpiritualActionReiteration.text = it.timesCount!!.value.toString().plus(" times per day")
                       RepeatType.WEEK -> textViewSpiritualActionReiteration.text = it.weekList!!.value.let {
                           it!!.joinToString(separator = " | ") { it.toString().subSequence(0, 2) }
                       }
                       RepeatType.MONTH -> textViewSpiritualActionReiteration.text = it.monthRange!!.value.toString()
                   }
               }
           }
            listContainer.addView(childBinding.root.rootView)
        }*/
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onComplete: (item: GoalEntity) -> Unit,
            onEdit: (item: GoalEntity) -> Unit
        ): GoalViewHolder {
            val binding: GoalsListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.goals_list_item,
                parent,
                false
            )
            return GoalViewHolder(binding, onComplete, onEdit)
        }
    }
}
