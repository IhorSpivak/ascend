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
        val mmName = arguments!!.getString(MM_NAME)
        val arguments = arguments!!.getParcelable<GroupsArg>(GROUP_FILTERS)

        binding.vpGroups.adapter = GroupsTabAdapter.newInstance(
            context!!,
            childFragmentManager,
            arguments!!
        )
        binding.tlGroups.setupWithViewPager(binding.vpGroups)

        binding.tvTitle.text = if(mmName!= null){
            getString(R.string.mm_name_groups,mmName)
        }else{
            getString(R.string.group_list)
        }
    }

    companion object {
        private const val GROUP_FILTERS = "GROUP_FILTERS"
        private const val MM_NAME = "MM_NAME"

        fun newInstance(args: GroupsArg, name: String?): GroupsFragment {
            val fragment = GroupsFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(GROUP_FILTERS, args)
                putString(MM_NAME, name)
            }
            return fragment
        }
    }
}