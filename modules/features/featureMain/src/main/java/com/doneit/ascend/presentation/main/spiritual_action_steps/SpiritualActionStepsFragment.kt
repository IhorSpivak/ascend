package com.doneit.ascend.presentation.main.spiritual_action_steps

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentSpiritualActionStepsBinding
import com.doneit.ascend.presentation.main.spiritual_action_steps.common.TabAdapter
import org.kodein.di.generic.instance


class SpiritualActionStepsFragment: BaseFragment<FragmentSpiritualActionStepsBinding>() {
    override val viewModelModule = SpiritualActionStepsViewModelModule.get(this)
    override val viewModel: SpiritualActionStepsContract.ViewModel by instance()

    private val tabAdapter: TabAdapter by lazy {
        TabAdapter.newInstance(context!!, childFragmentManager)
    }
    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            tabAdapter = this@SpiritualActionStepsFragment.tabAdapter
            buttonBack.setOnClickListener {
                // viewModel must be non nullable
                viewModel?.goBack()
            }
            buttonAdd.setOnClickListener {
                // viewModel must be non nullable
                viewModel?.onAddClick()
            }
            tabs.setupWithViewPager(viewPager)
        }

    }

}