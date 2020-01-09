package com.doneit.ascend.presentation.profile.payments.payment_methods

import androidx.lifecycle.map
import com.doneit.ascend.domain.use_case.interactor.cards.CardsUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.toPresentation
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class PaymentMethodsViewModel(
    private val router: PaymentMethodsContract.Router,
    private val cardsUseCase: CardsUseCase
) : BaseViewModelImpl(), PaymentMethodsContract.ViewModel {

    override val payments =
        cardsUseCase.getAllCards().map { list -> list.map { it.toPresentation() } }

    override fun onAddPaymentMethodClick() {
        router.navigateToAddPaymentMethod()
    }

    override fun deletePaymentMethod(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}