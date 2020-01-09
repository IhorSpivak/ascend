package com.doneit.ascend.presentation.profile.payments.payment_methods

import androidx.lifecycle.*
import com.doneit.ascend.domain.use_case.interactor.cards.CardsUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.PresentationCardModel
import com.doneit.ascend.presentation.models.toPresentation
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class PaymentMethodsViewModel(
    private val router: PaymentMethodsContract.Router,
    private val cardsUseCase: CardsUseCase
) : BaseViewModelImpl(), PaymentMethodsContract.ViewModel {

    override val payments = MediatorLiveData<List<PresentationCardModel>>()
    private var lastSource: LiveData<List<PresentationCardModel>>? = null

    init {
        updateCards()
    }


    override fun onAddPaymentMethodClick() {
        router.navigateToAddPaymentMethod()
    }

    override fun deletePaymentMethod(id: Long) {
        viewModelScope.launch {
            val result = cardsUseCase.deleteCard(id)

            if(result.isSuccessful) {
                updateCards()
            } else {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
        }
    }

    private fun updateCards() {
        if(lastSource != null) {
            payments.removeSource(lastSource!!)
        }

        lastSource = cardsUseCase.getAllCards().map { list -> list.map { it.toPresentation() } }
        payments.addSource(lastSource!!){
            payments.postValue(it)
        }
    }
}