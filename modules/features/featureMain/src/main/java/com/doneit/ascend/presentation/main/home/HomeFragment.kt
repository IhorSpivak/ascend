package com.doneit.ascend.presentation.main.home

import android.os.Bundle
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.common.ToolbarListener
import com.doneit.ascend.presentation.main.databinding.FragmentHomeBinding
import com.doneit.ascend.presentation.main.home.common.FollowerAdapter
import com.doneit.ascend.presentation.main.home.common.TabAdapter
import org.kodein.di.generic.instance

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val viewModelModule = HomeViewModelModule.get(this)
    override val viewModel: HomeContract.ViewModel by instance()

    private val adapter: TabAdapter by lazy {
        TabAdapter.newInstance(this, childFragmentManager)
    }

    private val followerAdapter: FollowerAdapter by lazy {
        FollowerAdapter(mutableListOf())
    }

    override fun viewCreated(savedInstanceState: Bundle?) {

        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.adapter = adapter
        binding.followerAdapter = followerAdapter

        viewModel.user.observe(this, Observer {
            it?.let { userIt ->
                (activity as ToolbarListener).setTitle("${getString(R.string.main_title)} ${userIt.community}")
                (activity as ToolbarListener).backButtonChangeVisibility(false)
            }
        })
    }
}
