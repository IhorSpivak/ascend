package com.doneit.ascend.presentation.main.ascension_plan.spiritual_action_steps.list.common

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.RepeatType
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.SpiritualActionStepEntity
import com.doneit.ascend.domain.entity.ascension.spiritual_action_step.TimeCommitmentType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.SpiritualActionListItemBinding
import com.doneit.ascend.presentation.utils.extensions.toDayShortMonthYear
import java.util.*

class SpiritualActionStepViewHolder(
    private val binding: SpiritualActionListItemBinding,
    private val onComplete: (item: SpiritualActionStepEntity) -> Unit,
    private val onEdit: (item: SpiritualActionStepEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SpiritualActionStepEntity) {
        binding.apply {
            textViewSpiritualActionId.text = (position + 1).toString()
            item.name?.let { nameResId ->
                textViewSpiritualActionName.text = textViewSpiritualActionName.context.getString(nameResId)
            }
            item.timeCommitment.apply {
                textViewSpiritualActionCommitmentTime.context.let { context ->
                    when (type) {
                        TimeCommitmentType.MINUTE -> textViewSpiritualActionCommitmentTime.text = context.getString(R.string.value_minutes, value.toString())
                        TimeCommitmentType.HOUR -> textViewSpiritualActionCommitmentTime.text = context.getString(R.string.value_hours, value.toString())
                    }
                }
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
                }else{
                    Date(item.deadline).toDayShortMonthYear()
                }
            }
            buttonSpiritualActionInteraction.apply {
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
                if (item.isCompleted){
                    visibility = View.GONE
                }
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onComplete: (item: SpiritualActionStepEntity) -> Unit,
            onEdit: (item: SpiritualActionStepEntity) -> Unit
        ): SpiritualActionStepViewHolder {
            val binding: SpiritualActionListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.spiritual_action_list_item,
                parent,
                false
            )
            return SpiritualActionStepViewHolder(binding, onComplete, onEdit)
        }

        private const val FIRST = 1
        private const val SECOND = 2
    }
}