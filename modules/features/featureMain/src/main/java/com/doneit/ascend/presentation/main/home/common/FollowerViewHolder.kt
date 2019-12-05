package com.doneit.ascend.presentation.main.home.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.LifecycleViewHolder
import com.doneit.ascend.presentation.main.databinding.TemplateFollowerItemBinding

class FollowerViewHolder(
    private val binding: TemplateFollowerItemBinding
) : LifecycleViewHolder(binding.root) {

    fun bind(item: UserEntity) {

        binding.imageUrl = "" // TODO: change to url
        binding.name = item.name
        binding.rating = item.rating

        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): FollowerViewHolder {
            val binding: TemplateFollowerItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_follower_item,
                parent,
                false
            )

            return FollowerViewHolder(binding)
        }
    }
}