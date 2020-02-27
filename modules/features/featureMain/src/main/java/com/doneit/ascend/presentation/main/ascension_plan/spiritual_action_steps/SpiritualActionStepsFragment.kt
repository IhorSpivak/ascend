package com.doneit.ascend.presentation.main.ascension_plan.spiritual_action_steps

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentSpiritualActionStepsBinding
import com.doneit.ascend.presentation.main.ascension_plan.spiritual_action_steps.common.TabAdapter
import org.kodein.di.generic.instance


class SpiritualActionStepsFragment: BaseFragment<FragmentSpiritualActionStepsBinding>() {
    override val viewModelModule = SpiritualActionStepsViewModelModule.get(this)
    override val viewModel: SpiritualActionStepsContract.ViewModel by instance()

    private val tabAdapter: TabAdapter by lazy {
        TabAdapter.newInstance(context!!, childFragmentManager)
    }
    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
        binding.apply {
            tabAdapter = this@SpiritualActionStepsFragment.tabAdapter
            tabs.setupWithViewPager(viewPager)
        }

    }

}