package com.doneit.ascend.presentation.main.groups.group_list.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateHorGroupItemBinding
import com.doneit.ascend.presentation.main.search.common.SearchViewHolder
import com.doneit.ascend.presentation.utils.ButtonType
import com.doneit.ascend.presentation.utils.getButonType

class GroupHorViewHolder(
    private val binding: TemplateHorGroupItemBinding
) : SearchViewHolder(binding.root) {

    fun bind(item: GroupEntity, user: UserEntity?, onButtonClick: (GroupEntity) -> Unit) {
        binding.item = item

        if(user == null) {
            hideButtons()
        } else {
            when(getButonType(user!!, item)) {
                ButtonType.START_GROUP -> {
                    binding.showStartButton = true
                    binding.btnStart.setOnClickListener {
                        onButtonClick.invoke(item)
                    }
                    binding.showJoinButton = false
                }
                ButtonType.JOIN_TO_DISCUSSION -> {
                    binding.showStartButton = false
                    binding.showJoinButton = true
                    binding.btnJoin.setOnClickListener {
                        onButtonClick.invoke(item)
                    }
                }
                else -> {
                    hideButtons()
                }
            }
        }

        binding.executePendingBindings()
    }

    private fun hideButtons() {
        binding.showJoinButton = false
        binding.showStartButton = false
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