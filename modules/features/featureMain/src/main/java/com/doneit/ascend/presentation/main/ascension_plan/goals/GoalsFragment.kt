package com.doneit.ascend.presentation.main.goals

import android.os.Bundle
import com.doneit.ascend.presentation.main.ascension_plan.goals.common.TabAdapter
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentGoalsBinding
import org.kodein.di.generic.instance

class GoalsFragment: BaseFragment<FragmentGoalsBinding>() {
    override val viewModelModule = GoalsViewModelModule.get(this)
    override val viewModel: GoalsContract.ViewModel by instance()

    private val tabAdapter: TabAdapter by lazy {
        TabAdapter.newInstance(context!!, childFragmentManager)
    }
    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.apply {
            tabAdapter = this@GoalsFragment.tabAdapter
            tabs.setupWithViewPager(viewPager)
        }
    }
}