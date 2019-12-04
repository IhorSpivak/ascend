package com.doneit.ascend.presentation.main.home

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.common.ToolbarListener
import com.doneit.ascend.presentation.main.databinding.FragmentHomeBinding
import com.doneit.ascend.presentation.main.home.common.TabAdapter
import org.kodein.di.generic.instance

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val viewModelModule = HomeViewModelModule.get(this)
    override val viewModel: HomeContract.ViewModel by instance()

    private val adapter: TabAdapter by lazy {
        TabAdapter.newInstance(this, childFragmentManager)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {

        binding.lifecycleOwner = this
        binding.model = viewModel
        binding.adapter = adapter
    }

    override fun onResume() {
        super.onResume()

        (activity as ToolbarListener).setTitle("Ascend")
        (activity as ToolbarListener).backButtonChangeVisibility(false)
    }
}
