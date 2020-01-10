package com.doneit.ascend.presentation.profile.payments.payment_methods.add_payment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentAddPaymentBinding
import com.doneit.ascend.presentation.utils.CardAssociation
import com.doneit.ascend.presentation.utils.getCardNumberType
import com.redmadrobot.inputmask.MaskedTextChangedListener
import org.kodein.di.generic.instance

class AddPaymentFragment : BaseFragment<FragmentAddPaymentBinding>() {

    override val viewModelModule = AddPaymentViewModelModule.get(this)
    override val viewModel: AddPaymentContract.ViewModel by instance()

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        setupExpiredDateMask()
        setupCardNumberMask()
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
                    CardAssociation.VISA -> binding.ivCardAssociation.setImageResource(R.drawable.ic_visa)
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

    companion object {
        private const val EXPIRED_DATE_INPUT_MASK = "[00]{/}[00]"
        private const val CARD_NUMBER_INPUT_MASK = "[0000]  [0000]  [0000]  [0000]"
    }
}