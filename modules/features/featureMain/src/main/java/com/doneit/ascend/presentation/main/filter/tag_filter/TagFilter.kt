package com.doneit.ascend.presentation.main.filter.tag_filter

import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.view.children
import androidx.databinding.DataBindingUtil
import com.doneit.ascend.domain.entity.TagEntity
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.common.visible
import com.doneit.ascend.presentation.main.databinding.TagChipViewBinding
import com.doneit.ascend.presentation.main.filter.base_filter.BaseFilter
import com.google.android.material.chip.Chip

abstract class TagFilter<T : TagFilterModel> : BaseFilter<T>() {

    abstract override val viewModel: TagFilterAbstractContract.ViewModel<T>

    private var initWithTagId: Int? = null

    override fun viewCreated(savedInstanceState: Bundle?) {
        super.viewCreated(savedInstanceState)
        observeData()
    }

    override fun setupBinding() = with(binding) {
        super.setupBinding()
        tvTagsTitle.visible()
        chipGroup.visible()
    }

    protected open fun observeData() = with(viewModel) {
        observe(tags, ::handleTags)
    }

    override fun initFilter(initFilter: T) {
        super.initFilter(initFilter)
        initWithTagId = initFilter.selectedTagId
    }

    private fun handleTags(tags: List<TagEntity>) = with(binding) {
        chipGroup.removeAllViews()
        tags.forEach { tag ->
            val binding: TagChipViewBinding = DataBindingUtil.inflate(
                LayoutInflater.from(requireContext()),
                R.layout.tag_chip_view,
                chipGroup,
                false
            )
            binding.tag = tag
            val chip = binding.root
            chip.tag = tag
            chipGroup.addView(chip)
        }
        chipGroup.setOnCheckedChangeListener { group, checkedId ->
            val tag = group.findViewById<Chip>(checkedId)?.tag as? TagEntity
            tag?.let { viewModel.tagSelected(it) }
        }
        expand()
        initWithTagId ?: return@with
        val id = chipGroup.children.firstOrNull { (it.tag as? TagEntity)?.id == initWithTagId }?.id
        chipGroup.check(id ?: return@with)
    }
}