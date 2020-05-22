package com.doneit.ascend.presentation.main.groups.daily_group_list

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.common.SideListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.databinding.FragmentGroupsListDailyBinding
import com.doneit.ascend.presentation.main.groups.group_list.GroupListArgs
import com.doneit.ascend.presentation.main.groups.group_list.common.GroupHorListAdapter
import com.doneit.ascend.presentation.utils.showDefaultError
import org.kodein.di.generic.instance

class GroupDailyListFragment : ArgumentedFragment<FragmentGroupsListDailyBinding, GroupListArgs>() {

    override val viewModelModule = GroupDailyListViewModelModule.get(this)
    override val viewModel: GroupDailyListContract.ViewModel by instance()

    private val adapter: GroupHorListAdapter by lazy {
        GroupHorListAdapter(null,
            {
                viewModel.onGroupClick(it)
            },
            {
                if (it.blocked != true) {
                    viewModel.onStartChatClick(it.id, it.groupType!!)
                } else {
                    showDefaultError(getString(R.string.error_group_user_removed))
                }
            },
            null
        )
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.rvGroups.adapter = adapter
        val decorator =
            SideListDecorator(paddingTop = resources.getDimension(R.dimen.groups_list_top_padding).toInt())
        binding.rvGroups.addItemDecoration(decorator)

        viewModel.groups.observe(viewLifecycleOwner, Observer {
            adapter.setUser(it.user)
            adapter.submitList(it.groups)
        })
    }

    companion object {
        fun newInstance(args: GroupListArgs): GroupDailyListFragment {
            val fragment = GroupDailyListFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(KEY_ARGS, args)
            }
            return fragment
        }
    }
}