package com.doneit.ascend.presentation.profile.payments.payment_methods

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import com.doneit.ascend.presentation.common.DividerItemDecorator
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentPaymentMethodsBinding
import com.doneit.ascend.presentation.profile.payments.payment_methods.common.PaymentMethodsAdapter
import org.kodein.di.generic.instance

class PaymentMethodsFragment : BaseFragment<FragmentPaymentMethodsBinding>() {

    override val viewModelModule = PaymentMethodsViewModelModule.get(this)
    override val viewModel: PaymentMethodsContract.ViewModel by instance()
    private val cardsAdapter = PaymentMethodsAdapter {
        viewModel.deletePaymentMethod(it)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        binding.rvCards.adapter = cardsAdapter
        binding.rvCards.addItemDecoration(DividerItemDecorator(context!!))

        viewModel.payments.observe(viewLifecycleOwner, Observer {
            cardsAdapter.setData(it)
        })
    }
}