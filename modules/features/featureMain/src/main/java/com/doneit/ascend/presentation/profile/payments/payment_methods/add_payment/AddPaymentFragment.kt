package com.doneit.ascend.presentation.profile.payments.payment_methods.add_payment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentAddPaymentBinding
import com.doneit.ascend.presentation.utils.extensions.showKeyboard
import com.doneit.ascend.presentation.utils.CardAssociation
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.getCardNumberType
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.stripe.android.PaymentConfiguration
import com.stripe.android.Stripe
import kotlinx.android.synthetic.main.fragment_add_payment.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class AddPaymentFragment : BaseFragment<FragmentAddPaymentBinding>() {

    override val kodeinModule = Kodein.Module(this::class.java.simpleName) {
        bind<Stripe>() with singleton { Stripe(activity!!.applicationContext, PaymentConfiguration.getInstance(activity!!.applicationContext).publishableKey) }
    }
    override val viewModelModule = AddPaymentViewModelModule.get(this)
    override val viewModel: AddPaymentContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        setupExpiredDateMask()
        setupCardNumberMask()

        viewModel.state.observe(viewLifecycleOwner, Observer {
            onStateChanged(it)
        })

        btnDone.setOnClickListener {
            viewModel.onDoneClick()
            hideKeyboard()
        }
    }

    private fun onStateChanged(state: AddPaymentState) {
        when(state){
            AddPaymentState.NUMBER -> binding.number.edit.showKeyboard()
            AddPaymentState.NAME -> binding.name.edit.showKeyboard()
            AddPaymentState.EXPIRATION -> binding.expiration.edit.showKeyboard()
            AddPaymentState.CVV -> binding.cvv.edit.showKeyboard()
        }
    }


    private fun setupExpiredDateMask(){
        val listener = MaskedTextChangedListener(EXPIRED_DATE_INPUT_MASK, binding.expiration.edit, object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }, object: MaskedTextChangedListener.ValueListener{
            override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {

            }
        })
        binding.expiration.edit.addTextChangedListener(listener)
        binding.expiration.edit.onFocusChangeListener = listener
    }

    private fun setupCardNumberMask(){
        val listener = MaskedTextChangedListener(CARD_NUMBER_INPUT_MASK, binding.number.edit, object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                when(s.toString().getCardNumberType())
                {
                    CardAssociation.INVALID -> binding.ivCardAssociation.setImageDrawable(null)
                    CardAssociation.VISA -> binding.ivCardAssociation.setImageResource(R.drawable.ic_visa_local)
                    CardAssociation.MASTERCARD -> binding.ivCardAssociation.setImageResource(R.drawable.ic_master_card_local)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        }, object: MaskedTextChangedListener.ValueListener {
            override fun onTextChanged(maskFilled: Boolean, extractedValue: String) {
            }
        })
        binding.number.edit.addTextChangedListener(listener)
        binding.number.edit.onFocusChangeListener = listener
    }

    override fun onDestroyView() {
        hideKeyboard()
        super.onDestroyView()
    }

    companion object {
        private const val EXPIRED_DATE_INPUT_MASK = "[00]{/}[00]"
        private const val CARD_NUMBER_INPUT_MASK = "[0000]  [0000]  [0000]  [0000]"
    }
}