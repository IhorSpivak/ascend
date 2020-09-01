package com.doneit.ascend.presentation.main.filter.tag_filter

import android.os.Bundle
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class TagFilterFragment : TagFilter<TagFilterModel>() {

    override val viewModelModule: Kodein.Module
        get() = TagFilterViewModelModule.get(this)

    override val viewModel: TagFilterContract.ViewModel by instance()

    companion object {
        fun newInstance(filter: TagFilterModel) = TagFilterFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_INIT_FILTER, filter)
            }
        }
    }
}