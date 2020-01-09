package com.doneit.ascend.presentation.profile.payments.add_payment

import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.use_case.interactor.cards.CardsUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.PresentationCreateCardModel
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule

@CreateFactory
@ViewModelDiModule
class AddPaymentViewModel(
    private val router: AddPaymentContract.Router,
    private val cardsUseCase: CardsUseCase
) : BaseViewModelImpl(), AddPaymentContract.ViewModel {

    override val dataModel = PresentationCreateCardModel()
    override val state = MutableLiveData<AddPaymentState>(AddPaymentState.NUMBER)

    override fun onBackStateClick() {
        state.value?.let {
            state.postValue(AddPaymentState.values()[it.ordinal - 1])
        }
    }

    override fun onForwardStateClick() {
        state.value?.let {
            state.postValue(AddPaymentState.values()[it.ordinal + 1])
        }
    }

    override fun onDoneClick() {
    }

    override fun onBackClick() {
        router.onBack()
    }
}