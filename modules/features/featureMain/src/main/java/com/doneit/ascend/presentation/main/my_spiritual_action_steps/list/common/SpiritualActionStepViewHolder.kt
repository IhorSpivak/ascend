package com.doneit.ascend.presentation.main.my_spiritual_action_steps.list.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.SpiritualActionStepEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.SpiritualActionListItemBinding

class SpiritualActionStepViewHolder(
    private val binding: SpiritualActionListItemBinding,
    private val onComplete: (item: SpiritualActionStepEntity) -> Unit,
    private val onEdit: (item: SpiritualActionStepEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root){

    fun bind(item: SpiritualActionStepEntity){
        binding.apply {
            textViewSpiritualActionId.text = item.id.toString()
            textViewSpiritualActionName.text = item.name
            //textViewSpiritualActionCommitmentTime.text = item.commitmentTime
            //textViewSpiritualActionReiteration.text = item.reiteration
            buttonSpiritualActionInteraction.setOnCreateContextMenuListener { menu, _, menuInfo ->  }
        }
    }

    companion object{
        fun create(
            parent: ViewGroup,
            onComplete: (item: SpiritualActionStepEntity) -> Unit,
            onEdit: (item: SpiritualActionStepEntity) -> Unit
        ): SpiritualActionStepViewHolder {
            val binding: SpiritualActionListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_master_mind,
                parent,
                false
            )
            return SpiritualActionStepViewHolder(binding, onComplete, onEdit)
        }
    }

}