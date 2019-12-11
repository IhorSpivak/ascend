package com.doneit.ascend.presentation.main.home

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentHomeBinding
import com.doneit.ascend.presentation.main.home.common.MastermindAdapter
import com.doneit.ascend.presentation.main.home.common.TabAdapter
import com.doneit.ascend.presentation.utils.extensions.visible
import org.kodein.di.generic.instance

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val viewModelModule = HomeViewModelModule.get(this)
    override val viewModel: HomeContract.ViewModel by instance()

    private val adapter: TabAdapter by lazy {
        TabAdapter.newInstance(this, childFragmentManager)
    }

    private val mastermindsAdapter: MastermindAdapter by lazy {
        MastermindAdapter(mutableListOf())
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.adapter = adapter
        binding.mastermindAdapter = mastermindsAdapter

        viewModel.updateData()

        viewModel.user.observe(this, Observer {
            setTitle(it?.community)
        })

        viewModel.masterMinds.observe(this, Observer {
            binding.masterMindPlaceholder.visible(it.isEmpty())
            binding.rvMasterminds.visible(it.isNotEmpty())

            mastermindsAdapter.updateData(it)
        })

        viewModel.isRefreshing.observe(this, Observer {
            binding.srLayout.isRefreshing = it
        })

        binding.srLayout.setOnRefreshListener {
            viewModel.updateData()
        }

        setTitle(viewModel.user.value?.community)
    }

    private fun setTitle(community: String?) {
        var title = getString(R.string.main_title)
        community?.let {
             title += " $community"
        }

        binding.tvTitle.text = title
    }
}
