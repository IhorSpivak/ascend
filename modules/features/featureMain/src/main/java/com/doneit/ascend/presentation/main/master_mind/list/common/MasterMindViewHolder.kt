package com.doneit.ascend.presentation.main.master_mind.list.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.LifecycleViewHolder
import com.doneit.ascend.presentation.main.databinding.TemplateMasterMindBinding

class MasterMindViewHolder(
    private val binding: TemplateMasterMindBinding
) : LifecycleViewHolder(binding.root) {

    fun bind(item: MasterMindEntity) {
        binding.item = item
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): MasterMindViewHolder {
            val binding: TemplateMasterMindBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.template_master_mind,
                parent,
                false
            )

            return MasterMindViewHolder(binding)
        }
    }
}