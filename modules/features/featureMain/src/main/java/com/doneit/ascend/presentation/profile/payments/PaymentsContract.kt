package com.doneit.ascend.presentation.profile.payments

import com.doneit.ascend.presentation.main.base.BaseViewModel

interface PaymentsContract {
    interface ViewModel : BaseViewModel {
        fun onBackClick()
    }

    interface Router {
        fun onBack()
    }
}