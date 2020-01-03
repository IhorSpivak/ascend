package com.doneit.ascend.presentation.profile.mm_following.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateMasterMindFollowedBinding

class FollowingViewHolder(
    private val binding: TemplateMasterMindFollowedBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: MasterMindEntity, followClick:(Long)->Unit, unfollowClick: (id: Long)->Unit) {
        binding.item = item

        binding.tvFollow.setOnClickListener {
            followClick.invoke(item.id)
        }

        binding.tvUnFollow.setOnClickListener {
            unfollowClick.invoke(item.id)
        }

        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): FollowingViewHolder {
            val binding: TemplateMasterMindFollowedBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_master_mind_followed,
                parent,
                false
            )

            return FollowingViewHolder(binding)
        }
    }
}