package com.doneit.ascend.presentation.profile.payments.payment_methods.add_payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.dto.CreateCardModel
import com.doneit.ascend.domain.use_case.interactor.cards.CardsUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.PresentationCreateCardModel
import com.doneit.ascend.presentation.models.ValidationResult
import com.doneit.ascend.presentation.models.toStripeCard
import com.doneit.ascend.presentation.utils.*
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Token
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class AddPaymentViewModel(
    private val router: AddPaymentContract.Router,
    private val cardsUseCase: CardsUseCase,
    private val stripe: Stripe
) : BaseViewModelImpl(), AddPaymentContract.ViewModel {

    override val dataModel = PresentationCreateCardModel()
    override val state = MutableLiveData<AddPaymentState>(AddPaymentState.NUMBER)
    override val canGoForward = MutableLiveData<Boolean>(false)

    init {
        dataModel.cardNumber.validator = { s ->
            val result = ValidationResult()
            if (s.isValidCardNumber().not()) {
                result.isSucceed = false
            }
            result
        }

        dataModel.name.validator = { s ->
            val result = ValidationResult()
            if (s.isValidName().not()) {
                result.isSucceed = false
            }
            result
        }

        dataModel.cvv.validator = { s ->
            val result = ValidationResult()
            if (s.isValidCVV().not()) {
                result.isSucceed = false
            }
            result
        }

        dataModel.expiration.validator = { s ->
            val result = ValidationResult()
            if (s.isValidExpiration().not()) {
                result.isSucceed = false
            }
            result
        }

        val invalidationListener = { updateGoForwardState(state.value!!) }
        dataModel.cardNumber.onFieldInvalidate = invalidationListener
        dataModel.name.onFieldInvalidate = invalidationListener
        dataModel.cvv.onFieldInvalidate = invalidationListener
        dataModel.expiration.onFieldInvalidate = invalidationListener
    }

    override fun onForwardStateClick() {
        state.value?.let {
            val nextState = AddPaymentState.values()[it.ordinal + 1]
            updateState(nextState)
        }
    }

    override fun onBackStateClick() {
        state.value?.let {
            val prevState = AddPaymentState.values()[it.ordinal - 1]
            updateState(prevState)
        }
    }

    override fun onDoneClick() {
        //todo replace by own request to StripeAPI?
        canGoForward.postValue(false)
        stripe.createToken(
            dataModel.toStripeCard(),
            dataModel.hashCode().toString(),
            object : ApiResultCallback<Token> {
                override fun onSuccess(result: Token) {
                    val tokenID = result.id
                    createCard(
                        CreateCardModel(
                            dataModel.name.observableField.getNotNull(),
                            tokenID
                        )
                    )

                }

                override fun onError(e: java.lang.Exception) {
                    canGoForward.postValue(true)
                    showDefaultErrorMessage(e.toString())
                }
            })
    }

    override fun onBackClick() {
        router.onBack()
    }

    private fun createCard(model: CreateCardModel) {
        viewModelScope.launch {
            val result = cardsUseCase.createCard(model)

            if (result.isSuccessful) {
                router.onBack()
            } else {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }

            canGoForward.postValue(true)
        }
    }

    private fun updateState(newState: AddPaymentState) {
        state.postValue(newState)
        updateGoForwardState(newState)
    }

    private fun updateGoForwardState(state: AddPaymentState) {
        when(state) {
            AddPaymentState.NUMBER -> canGoForward.postValue(dataModel.cardNumber.isValid)
            AddPaymentState.NAME -> canGoForward.postValue(dataModel.name.isValid)
            AddPaymentState.CVV -> canGoForward.postValue(dataModel.cvv.isValid)
            AddPaymentState.EXPIRATION -> canGoForward.postValue(dataModel.expiration.isValid)
        }
    }
}