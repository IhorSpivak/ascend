package com.doneit.ascend.presentation.profile.payments

import android.os.Bundle
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentPaymentsBinding
import com.doneit.ascend.presentation.profile.payments.common.PaymentsTabAdapter
import org.kodein.di.generic.instance

class PaymentsFragment : BaseFragment<FragmentPaymentsBinding>() {

    override val viewModelModule = PaymentsViewModelModule.get(this)
    override val viewModel: PaymentsContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        binding.tlPayments.setupWithViewPager(binding.vpPayments)
        binding.vpPayments.adapter = PaymentsTabAdapter.newInstance(this)
    }
}