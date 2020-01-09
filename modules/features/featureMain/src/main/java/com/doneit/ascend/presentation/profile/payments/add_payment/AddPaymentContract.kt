package com.doneit.ascend.presentation.profile.payments.add_payment

import androidx.lifecycle.LiveData
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.models.PresentationCreateCardModel

interface AddPaymentContract {
    interface ViewModel: BaseViewModel {
        val dataModel: PresentationCreateCardModel
        val state: LiveData<AddPaymentState>

        fun onBackClick()
        fun onBackStateClick()
        fun onForwardStateClick()
        fun onDoneClick()
    }

    interface Router {
        fun onBack()
    }
}