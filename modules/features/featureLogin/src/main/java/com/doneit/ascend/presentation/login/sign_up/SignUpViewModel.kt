package com.doneit.ascend.presentation.login.sign_up

import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.use_case.interactor.question.QuestionUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.models.PresentationSignUpModel
import com.doneit.ascend.presentation.login.models.ValidationResult
import com.doneit.ascend.presentation.login.models.toEntity
import com.doneit.ascend.presentation.login.sign_up.verify_phone.VerifyPhoneContract
import com.doneit.ascend.presentation.login.utils.*
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.models.PresentationMessage
import com.doneit.ascend.presentation.utils.Constants
import com.doneit.ascend.presentation.utils.LocalStorage
import com.doneit.ascend.presentation.utils.Messages
import com.doneit.ascend.presentation.utils.UIReturnStep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask


class SignUpViewModel(
    private val userUseCase: UserUseCase,
    private val questionUseCase: QuestionUseCase,
    private val router: SignUpContract.Router,
    private val localStorage: LocalStorage
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
                    registrationModel.phoneCode.getNotNull(),
                    registrationModel.phone.observableField.getNotNull()
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

            if (s != registrationModel.password.observableField.getNotNull()) {
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
        router.navigateToVerifyPhone()
        canContinue.postValue(false)
    }

    override fun onVerifyClick() {
        canContinue.postValue(false)
        startTimer()

        viewModelScope.launch {
            val requestEntity = userUseCase.signUp(registrationModel.toEntity())
            canContinue.postValue(true)

            if (requestEntity.isSuccessful) {
                localStorage.saveSessionToken(requestEntity.successModel!!.token)
                launch(Dispatchers.Main) {

                    val questionsRequest =
                        questionUseCase.getList(requestEntity.successModel!!.token)

                    if (questionsRequest.isSuccessful) {
                        questionUseCase.insert(questionsRequest.successModel!!)

                        localStorage.saveUIReturnStep(UIReturnStep.FIRST_TIME_LOGIN)
                        router.navigateToFirstTimeLogin(questionsRequest.successModel!!)
                    }
                }
            } else {
                if (requestEntity.errorModel!!.isNotEmpty()) {
                    showErrorMessage(requestEntity.errorModel!!.first())
                }
            }
        }
    }

    override fun sendCode() {
        canResendCode.postValue(false)
        startTimer()

        viewModelScope.launch {
            val requestEntity = userUseCase.getConfirmationCode(registrationModel.getPhoneNumber())

            if (requestEntity.isSuccessful.not()) {
                if (requestEntity.errorModel!!.isNotEmpty()) {
                    showErrorMessage(requestEntity.errorModel!!.first())
                }
            }

            delay(Constants.RESEND_CODE_INTERVAL)
            canResendCode.postValue(true)
        }
    }

    private fun showErrorMessage(message: String) {
        errorMessage.postValue(
            PresentationMessage(
                Messages.EROR.getId(),
                null,
                message
            )
        )
    }

    override fun onBackClick() {
        registrationModel.clear()
        router.goBack()
    }

    private fun startTimer() {
        sendTimer = Timer()

        val calendar = Calendar.getInstance()
        calendar.add(Calendar.MINUTE, 1)
        end = calendar.timeInMillis

        canShowTimer.postValue(true)

        sendTimer?.schedule(timerTask {
            val currentTime = Calendar.getInstance().time
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