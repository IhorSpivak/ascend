package com.doneit.ascend.presentation.login.sign_up

import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.models.PresentationSignUpModel
import com.doneit.ascend.presentation.login.models.ValidationResult
import com.doneit.ascend.presentation.login.models.toEntity
import com.doneit.ascend.presentation.login.sign_up.verify_phone.VerifyPhoneContract
import com.doneit.ascend.presentation.login.utils.*
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
        registrationModel.name.validator = {s ->
            val result = ValidationResult()
            if(s.isValidName().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_full_name)
            }

            result
        }

        registrationModel.email.validator = {s ->
            val result = ValidationResult()
            if(s.isValidEmail().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_email)
            }
            result
        }

        registrationModel.phone.validator = {s ->
            val result = ValidationResult()
            if(isPhoneValid(registrationModel.phoneCode.getNotNull(), registrationModel.phone.observableField.getNotNull()).not()) {
                result.isSussed = false
                result.errors.add(R.string.error_phone)
            }
            result
        }

        registrationModel.password.validator = {s ->
            val result = ValidationResult()

            if(s.isValidPassword().not()){
                result.isSussed = false
                result.errors.add(R.string.error_password)
            }

            result
        }

        registrationModel.passwordConfirmation.validator = {s ->
            val result = ValidationResult()

            if(s != registrationModel.password.observableField.getNotNull()){
                result.isSussed = false
                result.errors.add(R.string.error_password_confirm)
            }

            result
        }

        val invalidationListener = { invalidateFields() }
        registrationModel.name.onFieldInvalidate = invalidationListener
        registrationModel.email.onFieldInvalidate = invalidationListener
        registrationModel.phone.onFieldInvalidate = invalidationListener
        registrationModel.password.onFieldInvalidate = invalidationListener
        registrationModel.passwordConfirmation.onFieldInvalidate = invalidationListener

        registrationModel.hasAgreed.addOnPropertyChangedCallback(object: Observable.OnPropertyChangedCallback(){
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                invalidationListener()
            }
        })
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
        isValid = isValid and registrationModel.email.isValid
        isValid = isValid and registrationModel.phone.isValid
        isValid = isValid and registrationModel.password.isValid
        isValid = isValid and registrationModel.passwordConfirmation.isValid
        isValid = isValid and registrationModel.hasAgreed.getNotNull()
        canContinue.postValue(isValid)
    }
}