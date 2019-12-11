package com.doneit.ascend.presentation.main.home.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.LifecycleViewHolder
import com.doneit.ascend.presentation.main.databinding.TemplateFollowerItemBinding

class MastermindViewHolder(
    private val binding: TemplateFollowerItemBinding
) : LifecycleViewHolder(binding.root) {

    fun bind(item: MasterMindEntity) {

        binding.imageUrl = item.image?.url
        binding.name = item.fullName
        if(item.rated) {
            binding.rbRating.visibility = View.VISIBLE
            binding.rating = item.rating
        }

        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): MastermindViewHolder {
            val binding: TemplateFollowerItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_follower_item,
                parent,
                false
            )

            return MastermindViewHolder(binding)
        }
    }
}