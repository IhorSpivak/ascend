package com.doneit.ascend.presentation.profile.payments.payment_methods

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCardModel

interface PaymentMethodsContract {
    interface ViewModel : BaseViewModel {
        val payments: LiveData<List<PresentationCardModel>>

        fun updateCards()
        fun onAddPaymentMethodClick()
        fun deletePaymentMethod(id: Long)
    }

    interface Router {
        fun navigateToAddPaymentMethod()
    }
}