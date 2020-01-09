package com.doneit.ascend.presentation.profile.payments.add_payment

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface AddPaymentContract {
    interface ViewModel: BaseViewModel {

    }

    interface Router {
        fun onBack()
    }
}