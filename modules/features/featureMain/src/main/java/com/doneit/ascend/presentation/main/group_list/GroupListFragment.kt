package com.doneit.ascend.presentation.main.group_list

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.databinding.FragmentGroupListBinding
import com.doneit.ascend.presentation.main.group_list.common.GroupListAdapter
import com.doneit.ascend.presentation.main.group_list.common.GroupListArgs
import org.kodein.di.generic.instance

class GroupListFragment : ArgumentedFragment<FragmentGroupListBinding, GroupListArgs>() {

    override val viewModelModule =
        GroupListViewModelModule.get(
            this
        )
    override val viewModel: GroupListContract.ViewModel by instance()

    private val adapter: GroupListAdapter by lazy {
        GroupListAdapter(mutableListOf())
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.adapter = adapter
    }
}