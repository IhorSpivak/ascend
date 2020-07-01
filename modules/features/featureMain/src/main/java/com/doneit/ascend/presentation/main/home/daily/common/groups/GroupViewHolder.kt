package com.doneit.ascend.presentation.main.home.daily.common.groups

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.LifecycleViewHolder
import com.doneit.ascend.presentation.main.databinding.TemplateGroupItemBinding
import com.doneit.ascend.presentation.utils.convertCommunityToResId

class GroupViewHolder(
    private val binding: TemplateGroupItemBinding
) : LifecycleViewHolder(binding.root) {

    fun bind(item: GroupEntity, user: UserEntity?, onButtonClick: (GroupEntity) -> Unit) {
        binding.apply {
            this.item = item
            this.user = user
            this.theme = if (item.pastMeetingsCount == item.meetingsCount) {
                item.pastMeetingsCount?.let { item.themes?.get(it - 1) }
            } else {
                item.pastMeetingsCount?.let { item.themes?.get(it) }
            }
            convertCommunityToResId(
                user?.community.orEmpty(),
                item.groupType
            )?.let {
                tvGroupType.setText(it)
            }
            btnStartGroup.setOnClickListener {
                onButtonClick.invoke(item)
            }
            btnJoinToDisc.setOnClickListener {
                onButtonClick.invoke(item)
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

            return GroupViewHolder(
                binding
            )
        }
    }
}