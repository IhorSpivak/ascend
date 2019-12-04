package com.doneit.ascend.presentation.main.daily

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentDailyBinding
import org.kodein.di.generic.instance

class DailyFragment : BaseFragment<FragmentDailyBinding>() {

    override val viewModelModule = DailyViewModelModule.get(this)
    override val viewModel: DailyContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {

        binding.lifecycleOwner = this
        binding.model = viewModel
    }
}