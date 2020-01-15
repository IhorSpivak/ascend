package com.doneit.ascend.presentation.profile.payments.earnings

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentEarningsBinding
import org.kodein.di.generic.instance

class EarningsFragment : BaseFragment<FragmentEarningsBinding>() {

    override val viewModelModule = EarningsViewModelModule.get(this)
    override val viewModel: EarningsContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
    }
}