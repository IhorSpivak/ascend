package com.doneit.ascend.presentation.main.group_info.attendees.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateAttendeeItemBinding

class AttendeeItemViewHolder(
    private val binding: TemplateAttendeeItemBinding,
    private val onRemove: (member: AttendeeEntity) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(member: AttendeeEntity){
        binding.apply {
            user = member
            remove.setOnClickListener {
                onRemove.invoke(member)
            }
        }

    }

    companion object {
        fun create(
            parent: ViewGroup,
            onRemove: (member: AttendeeEntity) -> Unit
        ): AttendeeItemViewHolder {
            val binding: TemplateAttendeeItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_attendee_item,
                parent,
                false
            )
            return AttendeeItemViewHolder(binding, onRemove)
        }
    }
}