package com.doneit.ascend.presentation.main.create_group.add_member.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemMemberBinding
import com.doneit.ascend.presentation.main.group_info.attendees.AttendeesContract

class MemberViewHolder(
    private val binding: ListItemMemberBinding,
    private val onAdd: (member: AttendeeEntity) -> Unit,
    private val onRemove: (member: AttendeeEntity) -> Unit,
    val model: AddMemberViewModel
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(member: AttendeeEntity?){
        binding.apply {
            user = member
            modelView = model
            this.setSelection(false)
            model.selectedMembers.forEach {
                if (it.email == member!!.email){
                    this.setSelection(true)
                }
            }
        }
        binding.apply {
            root.setOnClickListener {
                if (it.isSelected){
                    onRemove.invoke(member!!)
                    this.swap()
                }else{
                    if (model.canAddMembers.value!!) {
                        onAdd.invoke(member!!)
                        this.swap()
                    }
                }
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onAdd: (member: AttendeeEntity) -> Unit,
            onRemove: (member: AttendeeEntity) -> Unit,
            model: AddMemberViewModel
        ): MemberViewHolder {
            val binding: ListItemMemberBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_member,
                parent,
                false
            )
            return MemberViewHolder(binding, onAdd, onRemove, model)
        }
    }
    private fun View.swapSelection(){
        this.isSelected = !isSelected
    }
    private fun View.swapEnable(){
        this.isEnabled = !isEnabled
    }
    fun View.initSelection(){
        this.isEnabled = false
    }
    private fun ListItemMemberBinding.swap(){
        root.swapSelection()
        followerName.swapEnable()
        selection.swapEnable()
    }

    private fun ListItemMemberBinding.setSelection(state: Boolean){
        root.isSelected = state
        followerName.isEnabled = state
        selection.isEnabled = state
    }
}