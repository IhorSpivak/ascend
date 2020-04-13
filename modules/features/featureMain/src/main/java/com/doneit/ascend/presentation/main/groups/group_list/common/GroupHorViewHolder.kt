package com.doneit.ascend.presentation.main.groups.group_list.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateHorGroupItemBinding
import com.doneit.ascend.presentation.main.search.common.SearchViewHolder

class GroupHorViewHolder(
    private val binding: TemplateHorGroupItemBinding
) : SearchViewHolder(binding.root) {

    fun bind(item: GroupEntity, user: UserEntity?, onButtonClick: (GroupEntity) -> Unit) {
        binding.apply {
            this.item = item
            community = user?.community
            this.user = user
            theme = if (item.passedCount == item.meetingsCount){
                item.themes?.get(item.passedCount -1)
            }else{
                item.themes?.get(item.passedCount)
            }
        }
        if(user == null) {
            hideButtons()
        } else {
            binding.apply {
                btnStart.setOnClickListener {
                    onButtonClick.invoke(item)
                }
                btnJoin.setOnClickListener {
                    onButtonClick.invoke(item)
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