package com.doneit.ascend.presentation.login.sign_up

import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.getDefaultCalendar
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.models.PresentationSignUpModel
import com.doneit.ascend.presentation.login.models.ValidationResult
import com.doneit.ascend.presentation.login.models.toEntity
import com.doneit.ascend.presentation.login.sign_up.verify_phone.VerifyPhoneContract
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.*
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask


class SignUpViewModel(
    private val userUseCase: UserUseCase,
    private val router: SignUpContract.Router
) : BaseViewModelImpl(), SignUpContract.ViewModel, VerifyPhoneContract.ViewModel {

    override val registrationModel = PresentationSignUpModel()
    override val canContinue = MutableLiveData<Boolean>()
    override val canResendCode = MutableLiveData<Boolean>(true)
    override val canShowTimer = MutableLiveData<Boolean>(false)
    override val timerValue = MutableLiveData<String>()
    override var sendTimer: Timer? = null
    override var end: Long = 0

    init {
        registrationModel.name.validator = { s ->
            val result = ValidationResult()
            if (s.isValidName().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_full_name)
            }

            result
        }

        registrationModel.email.validator = { s ->
            val result = ValidationResult()
            if (s.isValidEmail().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_email)
            }
            result
        }

        registrationModel.phone.validator = { s ->
            val result = ValidationResult()
            if (isPhoneValid(
                    registrationModel.phoneCode.getNotNullString(),
                    registrationModel.phone.observableField.getNotNullString()
                ).not()
            ) {
                result.isSussed = false
                result.errors.add(R.string.error_phone)
            }
            result
        }

        registrationModel.password.validator = { s ->
            val result = ValidationResult()

            if (s.isValidPassword().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_password)
            }

            result
        }

        registrationModel.passwordConfirmation.validator = { s ->
            val result = ValidationResult()

            if (s != registrationModel.password.observableField.getNotNullString()) {
                result.isSussed = false
                result.errors.add(R.string.error_password_confirm)
            }

            result
        }

        registrationModel.code.validator = { s ->
            val result = ValidationResult()

            if (s.isValidConfirmationCode().not()) {
                result.isSussed = false
            }

            result
        }

        val invalidationListener = { updateCanContinue() }
        registrationModel.name.onFieldInvalidate = invalidationListener
        registrationModel.email.onFieldInvalidate = invalidationListener
        registrationModel.phone.onFieldInvalidate = invalidationListener
        registrationModel.password.onFieldInvalidate = invalidationListener
        registrationModel.passwordConfirmation.onFieldInvalidate = invalidationListener
        registrationModel.code.onFieldInvalidate = {
            canContinue.postValue(registrationModel.code.isValid)
        }

        registrationModel.hasAgreed.addOnPropertyChangedCallback(object :
            Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                invalidationListener()
            }
        })
    }


    override fun removeErrors() {
        registrationModel.name.removeError()
        registrationModel.email.removeError()
        registrationModel.phone.removeError()
        registrationModel.password.removeError()
        registrationModel.passwordConfirmation.removeError()
    }

    override fun continueClick() {
        canContinue.postValue(false)
        viewModelScope.launch {
            val requestEntity = userUseCase.signUpValidation(registrationModel.toEntity())

            if (requestEntity.isSuccessful) {
                router.navigateToVerifyPhone()
            } else {
                if (requestEntity.errorModel!!.isNotEmpty()) {
                    showDefaultErrorMessage(requestEntity.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun onVerifyClick() {
        canContinue.postValue(false)

        viewModelScope.launch {
            val requestEntity = userUseCase.signUp(registrationModel.toEntity())
            canContinue.postValue(true)

            if (requestEntity.isSuccessful) {

                requestEntity.successModel?.let {

                    if (it.userEntity.isMasterMind) {
                        router.goToMain()
                    } else {
                        router.navigateToFirstTimeLogin()
                    }
                }
            } else {
                if (requestEntity.errorModel!!.isNotEmpty()) {
                    showDefaultErrorMessage(requestEntity.errorModel!!.toErrorMessage())
                }
            }
        }
    }

    override fun sendCode(isStartTimer: Boolean) {
        canResendCode.postValue(false)

        if (isStartTimer) {
            startTimer()
        }

        viewModelScope.launch {
            val requestEntity = userUseCase.getConfirmationCode(registrationModel.getPhoneNumber())

            if (requestEntity.isSuccessful.not()) {
                if (requestEntity.errorModel!!.isNotEmpty()) {
                    showDefaultErrorMessage(requestEntity.errorModel!!.first())
                }
            }

            delay(Constants.RESEND_CODE_INTERVAL)
            canResendCode.postValue(true)
        }
    }

    override fun onBackClick(clearModel: Boolean) {

        if (clearModel) {
            registrationModel.clear()
        } else {
            updateCanContinue()
        }

        router.onBack()
    }

    override fun onTermsAndConditionsClick() {
        router.navigateToTerms()
    }

    override fun onPrivacyPolicyClick() {
        router.navigateToPrivacyPolicy()
    }

    private fun startTimer() {
        sendTimer = Timer()

        val calendar = getDefaultCalendar()
        calendar.add(Calendar.MINUTE, 1)
        end = calendar.timeInMillis

        canShowTimer.postValue(true)

        sendTimer?.schedule(timerTask {
            val currentTime = getDefaultCalendar().time
            val diffInMs = end - currentTime.time

            if (diffInMs <= 0) {
                canShowTimer.postValue(false)

                sendTimer?.cancel()
                sendTimer = null
                return@timerTask
            }

            val diffInSec: Long = TimeUnit.MILLISECONDS.toSeconds(diffInMs)
            timerValue.postValue(
                String.format(
                    "00:%s",
                    if (diffInSec < 10) "0${diffInSec}" else "$diffInSec"
                )
            )
        }, 0, 1000)
    }

    private fun updateCanContinue() {
        var isFormValid = true

        isFormValid = isFormValid and registrationModel.name.isValid
        isFormValid = isFormValid and registrationModel.email.isValid
        isFormValid = isFormValid and registrationModel.phone.isValid
        isFormValid = isFormValid and registrationModel.password.isValid
        isFormValid = isFormValid and registrationModel.passwordConfirmation.isValid
        isFormValid = isFormValid and registrationModel.hasAgreed.getNotNull()

        canContinue.postValue(isFormValid)
    }
}