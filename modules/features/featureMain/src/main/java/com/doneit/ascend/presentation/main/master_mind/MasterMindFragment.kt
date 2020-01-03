package com.doneit.ascend.presentation.main.master_mind

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentMasterMindBinding
import com.doneit.ascend.presentation.main.master_mind.common.TabAdapter
import org.kodein.di.generic.instance

class MasterMindFragment : BaseFragment<FragmentMasterMindBinding>() {

    override val viewModelModule = MasterMindViewModelModule.get(this)
    override val viewModel: MasterMindContract.ViewModel by instance()

    private val tabAdapter: TabAdapter by lazy {
        TabAdapter.newInstance(context!!, childFragmentManager)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.tabAdapter = this.tabAdapter

        binding.tlMasterMinds.setupWithViewPager(binding.vpMasterMinds)

        binding.btnBack.setOnClickListener {
            viewModel.goBack()
        }

        binding.btnSearch.setOnClickListener {
            viewModel.onSearchClick()
        }
    }
}