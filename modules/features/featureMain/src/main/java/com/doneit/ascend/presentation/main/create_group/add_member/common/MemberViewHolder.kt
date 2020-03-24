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
    private val isPublic: Boolean,
    private val onAdd: (member: AttendeeEntity) -> Unit,
    private val onRemove: (member: AttendeeEntity) -> Unit,
    private val onInviteClick: (email: String) -> Unit,
    val model: AddMemberViewModel
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(member: AttendeeEntity?){
        binding.apply {
            user = member
            modelView = model
            this.setSelection(false)
            if (model.selectedMembers.contains(member)){
                this.setSelection(true)
            }
        }
        binding.apply {
            root.setOnClickListener {
                if (it.isSelected){
                    onRemove.invoke(member!!)
                    this.swap()
                }else{
                    if (model.selectedMembers.size < MAX_MEMBERS_COUNT) {
                        onAdd.invoke(member!!)
                        this.swap()
                    }
                }
            }
        }
    }

    companion object {
        const val MAX_MEMBERS_COUNT = 50
        fun create(
            parent: ViewGroup,
            isPublic: Boolean,
            onAdd: (member: AttendeeEntity) -> Unit,
            onRemove: (member: AttendeeEntity) -> Unit,
            onInviteClick: (email: String) -> Unit,
            model: AddMemberViewModel
        ): MemberViewHolder {
            val binding: ListItemMemberBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_member,
                parent,
                false
            )
            return MemberViewHolder(binding, isPublic, onAdd, onRemove, onInviteClick, model)
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