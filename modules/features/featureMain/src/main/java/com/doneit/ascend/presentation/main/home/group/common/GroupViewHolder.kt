package com.doneit.ascend.presentation.main.home.group.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.LifecycleViewHolder
import com.doneit.ascend.presentation.main.databinding.TemplateGroupItemBinding
import com.doneit.ascend.presentation.utils.ButtonType
import com.doneit.ascend.presentation.utils.getButonType

class GroupViewHolder(
    private val binding: TemplateGroupItemBinding
) : LifecycleViewHolder(binding.root) {

    fun bind(item: GroupEntity, user: UserEntity?, onButtonClick: (GroupEntity) -> Unit) {
        binding.item = item

        when(getButonType(user!!, item)) {
            ButtonType.START_GROUP -> {
                binding.showStartButton = true
                binding.btnStartGroup.setOnClickListener {
                    onButtonClick.invoke(item)
                }
                binding.showJoinButton = false
            }
            ButtonType.JOIN_TO_DISCUSSION -> {
                binding.showStartButton = false
                binding.showJoinButton = true
                binding.btnJoinToDisc.setOnClickListener {
                    onButtonClick.invoke(item)
                }
            }
            else -> {
                binding.showJoinButton = false
                binding.showStartButton = false
            }
        }

        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): GroupViewHolder {
            val binding: TemplateGroupItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_group_item,
                parent,
                false
            )

            return GroupViewHolder(binding)
        }
    }
}