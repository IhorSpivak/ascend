package com.doneit.ascend.presentation.main.group_list.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.LifecycleViewHolder
import com.doneit.ascend.presentation.main.databinding.TemplateHorGroupItemBinding

class GroupHorViewHolder(
    private val binding: TemplateHorGroupItemBinding
) : LifecycleViewHolder(binding.root) {

    fun bind(item: GroupEntity) {
        binding.item = item
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): GroupHorViewHolder {
            val binding: TemplateHorGroupItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_hor_group_item,
                parent,
                false
            )

            return GroupHorViewHolder(binding)
        }
    }
}