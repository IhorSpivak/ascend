package com.doneit.ascend.presentation.login.sign_up

import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.models.PresentationSignUpModel
import com.doneit.ascend.presentation.login.models.ValidationResult
import com.doneit.ascend.presentation.login.models.toEntity
import com.doneit.ascend.presentation.login.sign_up.verify_phone.VerifyPhoneContract
import com.vrgsoft.core.presentation.fragment.BaseViewModelImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SignUpViewModel(
    private val userUseCase: UserUseCase,
    private val router: SignUpContract.Router
): BaseViewModelImpl(), SignUpContract.ViewModel, VerifyPhoneContract.ViewModel {

    override val registrationModel = PresentationSignUpModel()
    override val canContinue = MutableLiveData<Boolean>()

    init {
        //todo move errors messages to res
        registrationModel.name.validator = {s ->
            val result = ValidationResult()
            if(s.length < 4) {
                result.isSussed = false
                result.errors.add("Please, enter your Full Name correctly")
            }

            invalidateFields()

            result
        }

        registrationModel.email.validator = {s ->
            val result = ValidationResult()

            result
        }
    }

    override fun continueClick() {
        router.navigateToVerifyPhone()

    }

    override fun onVerifyClick() {
        GlobalScope.launch {
            val requestEntity = userUseCase.signUp(registrationModel.toEntity())

            if(requestEntity.isSuccessful){
                launch(Dispatchers.Main) {
                    router.goToMain()
                }
            }
        }
    }

    override fun sendCode() {
        GlobalScope.launch {
            userUseCase.getConfirmationCode(registrationModel.getPhoneNumber())
        }
    }

    override fun onBackClick() {
        router.goBack()
    }

    private fun invalidateFields() {
        var isValid = true
        isValid = isValid and registrationModel.name.isValid
        canContinue.postValue(isValid)
    }
}