package com.doneit.ascend.presentation.main.groups.group_list

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.common.TopListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.databinding.FragmentGroupListBinding
import com.doneit.ascend.presentation.main.groups.group_list.common.GroupListAdapter
import org.kodein.di.generic.instance

class GroupListFragment : ArgumentedFragment<FragmentGroupListBinding, GroupListArgs>() {

    override val viewModelModule = GroupListViewModelModule.get(this)
    override val viewModel: GroupListContract.ViewModel by instance()

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
        binding.model = viewModel
        binding.rvGroups.adapter = adapter
        val decorator =
            TopListDecorator(resources.getDimension(R.dimen.groups_list_top_padding).toInt())
        binding.rvGroups.addItemDecoration(decorator)

        viewModel.groups.observe(viewLifecycleOwner, Observer {
            adapter.setUser(it.user)
            adapter.submitList(it.groups)
        })
    }

    companion object {
        fun newInstance(args: GroupListArgs): GroupListFragment {
            val fragment = GroupListFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(KEY_ARGS, args)
            }
            return fragment
        }
    }
}