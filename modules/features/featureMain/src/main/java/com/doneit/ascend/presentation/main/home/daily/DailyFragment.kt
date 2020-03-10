package com.doneit.ascend.presentation.main.home.daily

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.common.SideListDecorator
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentDailyBinding
import com.doneit.ascend.presentation.main.home.daily.common.groups.GroupAdapter
import com.doneit.ascend.presentation.main.home.daily.common.master_minds.MastermindAdapter
import com.doneit.ascend.presentation.utils.showDefaultError
import org.kodein.di.generic.instance

class DailyFragment : BaseFragment<FragmentDailyBinding>() {

    override val viewModelModule = DailyViewModelModule.get(this)
    override val viewModel: DailyContract.ViewModel by instance()

    private val groupsAdapter: GroupAdapter by lazy {
        GroupAdapter(mutableListOf(),
            null,
            {
                viewModel.onGroupClick(it.id)
            },
            {
                if (it.blocked != true) {
                    viewModel.onStartChatClick(it.id)
                } else {
                    showDefaultError(getString(R.string.error_group_user_removed))
                }
            })
    }
    private val mastermindsAdapter: MastermindAdapter by lazy {
        MastermindAdapter(
            mutableListOf()
        ) {
            viewModel.openProfile(it.id)
        }
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        //binding.hasGroups = true
        //binding.hasMasterMinds = true
        binding.networkStatus = (activity as BaseActivity).isNetworkAvailable
        val decorator = SideListDecorator(paddingLeft = resources.getDimension(R.dimen.create_group_horizontal_margin).toInt())
        binding.rvGroups.addItemDecoration(decorator)
        binding.rvMasterminds.addItemDecoration(decorator)
        binding.rvGroups.adapter = groupsAdapter
        binding.rvMasterminds.adapter = mastermindsAdapter

        viewModel.masterMinds.observe(viewLifecycleOwner, Observer {
            binding.apply {
                hasMasterMinds = it.isNotEmpty()
            }
            mastermindsAdapter.updateData(it)
        })

        viewModel.groups.observe(viewLifecycleOwner, Observer {
            binding.hasGroups = it.groups.isNotEmpty()
            groupsAdapter.setUser(it.user)
            groupsAdapter.submitList(it.groups)
        })

        viewModel.isRefreshing.observe(viewLifecycleOwner, Observer {
            binding.srLayout.isRefreshing = it
        })

        binding.srLayout.setOnRefreshListener {
            viewModel.updateData()
            binding.networkStatus = (activity as BaseActivity).isNetworkAvailable
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateData()
        binding.networkStatus = (activity as BaseActivity).isNetworkAvailable
    }
}