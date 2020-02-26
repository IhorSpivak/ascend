package com.doneit.ascend.presentation.main.my_spiritual_action_steps

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentMySpiritualActionStepsBinding
import com.doneit.ascend.presentation.main.my_spiritual_action_steps.common.TabAdapter
import org.kodein.di.generic.instance

class MySpiritualActionSteps: BaseFragment<FragmentMySpiritualActionStepsBinding>() {
    override val viewModelModule = MySpiritualActionStepsViewModelModule.get(this)
    override val viewModel: MySpiritualActionStepsContract.ViewModel by instance()

    private val tabAdapter: TabAdapter by lazy {
        TabAdapter.newInstance(context!!, childFragmentManager)
    }
    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            tabAdapter = this.tabAdapter
            viewModel = this@MySpiritualActionSteps.viewModel
            tabs.setupWithViewPager(viewPager)
        }
    }
}