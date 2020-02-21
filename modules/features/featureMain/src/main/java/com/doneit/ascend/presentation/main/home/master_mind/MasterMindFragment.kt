package com.doneit.ascend.presentation.main.home.master_mind

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentHomeGroupsBinding
import com.doneit.ascend.presentation.main.groups.group_list.common.GroupHorListAdapter
import com.doneit.ascend.presentation.utils.showDefaultError
import org.kodein.di.generic.instance

class MasterMindFragment : BaseFragment<FragmentHomeGroupsBinding>() {

    override val viewModelModule = MasterMindViewModelModule.get(this)
    override val viewModel: MasterMindContract.ViewModel by instance()

    private val adapter: GroupHorListAdapter by lazy {
        GroupHorListAdapter(null,
            {
                viewModel.onGroupClick(it.id)
            },
            {
                if (it.blocked != true) {
                    viewModel.onStartChatClick(it.id)
                } else {
                    showDefaultError(getString(R.string.error_group_user_removed))
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.rvGroups.adapter = adapter

        viewModel.groups.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.groups)
            binding.srLayout.isRefreshing = false
        })

        binding.srLayout.setOnRefreshListener {
            viewModel.updateData()
        }
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
    }
}