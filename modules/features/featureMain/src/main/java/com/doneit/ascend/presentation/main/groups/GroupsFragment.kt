package com.doneit.ascend.presentation.main.groups

import android.os.Bundle
import com.doneit.ascend.presentation.common.TopListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.databinding.FragmentGroupListBinding
import com.doneit.ascend.presentation.main.groups.common.GroupListAdapter
import com.doneit.ascend.presentation.main.groups.common.GroupsArgs
import org.kodein.di.generic.instance

class GroupsFragment : ArgumentedFragment<FragmentGroupListBinding, GroupsArgs>() {

    override val viewModelModule =
        GroupListViewModelModule.get(
            this
        )
    override val viewModel: GroupsContract.ViewModel by instance()

    private val adapter: GroupListAdapter by lazy {
        GroupListAdapter(null,
            {
                viewModel.onGroupClick(it)
            },
            {
                viewModel.onStartChatClick(it)
            }
        )
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.adapter = adapter

        val decorator =
            TopListDecorator(resources.getDimension(R.dimen.groups_list_top_padding).toInt())
        binding.rvGroups.addItemDecoration(decorator)
    }

    companion object {
        fun newInstance(args: GroupsArgs): GroupsFragment {
            val fragment = GroupsFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(KEY_ARGS, args)
            }
            return fragment
        }
    }
}