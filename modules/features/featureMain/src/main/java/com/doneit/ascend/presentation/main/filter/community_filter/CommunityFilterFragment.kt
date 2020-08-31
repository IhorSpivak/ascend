package com.doneit.ascend.presentation.main.filter.community_filter

import android.os.Bundle
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class CommunityFilterFragment : CommunityFilter<CommunityFilterModel>() {
    override val viewModelModule: Kodein.Module
        get() = CommunityFilterViewModelModule.get(this)

    override val viewModel: CommunityFilterContract.ViewModel by instance()

    companion object {
        fun newInstance(filter: CommunityFilterModel) = CommunityFilterFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_INIT_FILTER, filter)
            }
        }
    }
}