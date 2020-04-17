package com.doneit.ascend.presentation.main.home.master_mind

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.common.SideListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentHomeGroupsBinding
import com.doneit.ascend.presentation.main.groups.group_list.common.GroupHorListAdapter
import com.doneit.ascend.presentation.utils.showDefaultError
import org.kodein.di.generic.instance

class MasterMindFragment : BaseFragment<FragmentHomeGroupsBinding>() {

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
            },
            null
        )
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.apply {
            hasGroups = true
            networkStatus = (activity as BaseActivity).isNetworkAvailable
        }

        val decorator =
            SideListDecorator(paddingTop = resources.getDimension(R.dimen.search_list_top_padding).toInt())
        binding.rvGroups.addItemDecoration(decorator)
        binding.rvGroups.adapter = adapter

        viewModel.groups.observe(viewLifecycleOwner, Observer {
            binding.hasGroups = it.groups.isNullOrEmpty().not()
            binding.networkStatus = (activity as BaseActivity).isNetworkAvailable
            adapter.setUser(it.user)
            adapter.submitList(it.groups)
            binding.srLayout.isRefreshing = false
        })

        binding.srLayout.setOnRefreshListener {
            viewModel.updateData()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
        binding.networkStatus = (activity as BaseActivity).isNetworkAvailable
    }
}