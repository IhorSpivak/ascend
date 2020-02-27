package com.doneit.ascend.presentation.main.spiritual_action_steps.list.common

import android.view.*
import android.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.RepeatType
import com.doneit.ascend.domain.entity.SpiritualActionStepEntity
import com.doneit.ascend.domain.entity.TimeCommitmentType
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.SpiritualActionListItemBinding
import java.text.SimpleDateFormat
import java.util.*

class SpiritualActionStepViewHolder(
    private val binding: SpiritualActionListItemBinding,
    private val onComplete: (item: SpiritualActionStepEntity) -> Unit,
    private val onEdit: (item: SpiritualActionStepEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SpiritualActionStepEntity) {
        binding.apply {
            textViewSpiritualActionId.text = (position + 1).toString()
            textViewSpiritualActionName.text = item.name
            item.timeCommitment.apply {
                when(type){
                    TimeCommitmentType.MINUTE -> textViewSpiritualActionCommitmentTime.text = value.toString().plus(" min")
                    TimeCommitmentType.HOUR -> textViewSpiritualActionCommitmentTime.text = value.toString().plus(" hr")
                }
            }
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
                    SimpleDateFormat("dd MM yyyy", Locale.getDefault()).format(Date(item.deadline))
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