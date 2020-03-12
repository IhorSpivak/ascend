package com.doneit.ascend.presentation.main.master_mind.list.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.databinding.TemplateMasterMindBinding
import com.doneit.ascend.presentation.main.search.common.SearchViewHolder

class MasterMindViewHolder(
    private val binding: TemplateMasterMindBinding
) : SearchViewHolder(binding.root) {

    fun bind(item: MasterMindEntity, onSeeGroupsClick: (item: MasterMindEntity)->Unit) {
        binding.item = item

        binding.tvSeeGroups.setOnClickListener {
            onSeeGroupsClick.invoke(item)
        }

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