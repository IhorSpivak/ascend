package com.doneit.ascend.presentation.main.groups

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.databinding.FragmentGroupsBinding
import com.doneit.ascend.presentation.main.groups.common.GroupAdapter
import com.doneit.ascend.presentation.main.groups.common.GroupsArgs
import org.kodein.di.generic.instance

class GroupsFragment : ArgumentedFragment<FragmentGroupsBinding, GroupsArgs>() {

    override val viewModelModule = GroupsViewModelModule.get(this)
    override val viewModel: GroupsContract.ViewModel by instance()

    private val adapter: GroupAdapter by lazy {
        GroupAdapter(mutableListOf())
    }

    override fun viewCreated(savedInstanceState: Bundle?) {

        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.adapter = adapter
    }
}