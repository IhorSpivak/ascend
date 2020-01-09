package com.doneit.ascend.presentation.profile.payments

import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class PaymentsViewModel (
    private val router: PaymentsContract.Router
): BaseViewModelImpl(), PaymentsContract.ViewModel {
    override fun onBackClick() {
        router.onBack()
    }
}