package com.doneit.ascend.presentation.main.home.daily.common.groups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.group.GroupType
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.LifecycleViewHolder
import com.doneit.ascend.presentation.main.databinding.TemplateGroupItemBinding
import com.doneit.ascend.presentation.utils.ButtonType
import com.doneit.ascend.presentation.utils.getButtonType

class GroupViewHolder(
    private val binding: TemplateGroupItemBinding
) : LifecycleViewHolder(binding.root) {

    fun bind(item: GroupEntity, user: UserEntity?, onButtonClick: (GroupEntity) -> Unit) {
        binding.apply {
            this.item = item
            this.user = user
            when(item.groupType){
                GroupType.MASTER_MIND -> tvGroupType.text = root.context.resources.getString(R.string.master_mind_group)
                GroupType.INDIVIDUAL -> tvGroupType.text = root.context.resources.getString(R.string.master_mind_group)
                GroupType.SUPPORT -> tvGroupType.text = root.context.resources.getString(R.string.support_group)
                GroupType.WEBINAR -> tvGroupType.text = root.context.resources.getString(R.string.webinars)
            }
            btnStartGroup.setOnClickListener {
                onButtonClick.invoke(item)
            }
            btnJoinToDisc.setOnClickListener {
                onButtonClick.invoke(item)
            }
        }

/*
        when(getButtonType(user!!, item)) {
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
*/

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

            return GroupViewHolder(
                binding
            )
        }
    }
}