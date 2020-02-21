package com.doneit.ascend.presentation.main.groups

import android.os.Bundle
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentGroupsBinding
import com.doneit.ascend.presentation.main.groups.common.GroupsTabAdapter
import org.kodein.di.generic.instance

class GroupsFragment : BaseFragment<FragmentGroupsBinding>() {

    override val viewModelModule = GroupsViewModelModule.get(this)
    override val viewModel: GroupsContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        val arguments = arguments!!.getParcelable<GroupsArg>(GROUP_FILTERS)
        binding.vpGroups.adapter = GroupsTabAdapter.newInstance(
            context!!,
            childFragmentManager,
            arguments!!
        )
        binding.tlGroups.setupWithViewPager(binding.vpGroups)

        binding.tvTitle.text = getString(R.string.group_list)
    }

    companion object {
        private const val GROUP_FILTERS = "GROUP_FILTERS"

        fun newInstance(args: GroupsArg): GroupsFragment {
            val fragment = GroupsFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(GROUP_FILTERS, args)
            }
            return fragment
        }
    }
}