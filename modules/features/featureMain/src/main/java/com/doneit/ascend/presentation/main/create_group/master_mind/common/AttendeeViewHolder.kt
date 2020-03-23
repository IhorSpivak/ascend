package com.doneit.ascend.presentation.main.create_group.master_mind.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.AttendeeListItemBinding

class AttendeeViewHolder (
    private val binding: AttendeeListItemBinding,
    private val onDeleteMember: (AttendeeEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(member: AttendeeEntity){
        binding.apply {
            attendee = member
            memberNotAttended.setOnClickListener {
                onDeleteMember.invoke(member)
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            onDeleteMember: (AttendeeEntity) -> Unit
        ): AttendeeViewHolder {
            val binding: AttendeeListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.attendee_list_item,
                parent,
                false
            )
            return AttendeeViewHolder(binding, onDeleteMember)
        }
    }
}