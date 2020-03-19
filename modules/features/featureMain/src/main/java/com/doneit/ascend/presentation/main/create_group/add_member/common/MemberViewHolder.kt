package com.doneit.ascend.presentation.main.create_group.add_member.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.ListItemMemberBinding

class MemberViewHolder(
    private val binding: ListItemMemberBinding,
    private val isPublic: Boolean,
    private val onAdd: (member: AttendeeEntity) -> Unit,
    private val onRemove: (member: AttendeeEntity) -> Unit,
    private val onInviteClick: (email: String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(member: AttendeeEntity?){
        binding.user = member
        binding.apply {
            root.isSelected = false
            followerName.initSelection()
            selection.initSelection()
        }
        binding.apply {
            root.setOnClickListener {
                if (it.isSelected){
                    onRemove.invoke(member!!)
                }else{
                    onAdd.invoke(member!!)
                }
                it.swapSelection()
                followerName.apply {
                    isEnabled = !isEnabled
                }
                selection.apply {
                    isEnabled = !isEnabled
                }
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            isPublic: Boolean,
            onAdd: (member: AttendeeEntity) -> Unit,
            onRemove: (member: AttendeeEntity) -> Unit,
            onInviteClick: (email: String) -> Unit
        ): MemberViewHolder {
            val binding: ListItemMemberBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_member,
                parent,
                false
            )
            return MemberViewHolder(binding, isPublic, onAdd, onRemove, onInviteClick)
        }
    }
    fun View.swapSelection(){
        this.isSelected = !isSelected
    }
    fun View.initSelection(){
        this.isEnabled = false
    }
}