package com.doneit.ascend.presentation.profile.payments.earnings

import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class EarningsViewModel (
    private val router: EarningsContract.Router
): BaseViewModelImpl(), EarningsContract.ViewModel {

}