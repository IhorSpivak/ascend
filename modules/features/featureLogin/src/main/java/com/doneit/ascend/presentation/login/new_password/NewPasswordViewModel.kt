package com.doneit.ascend.presentation.login.new_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.getDefaultCalendar
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.models.PresentationNewPasswordModel
import com.doneit.ascend.presentation.login.models.ValidationResult
import com.doneit.ascend.presentation.login.models.toEntity
import com.doneit.ascend.presentation.login.new_password.common.NewPasswordArgs
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.PresentationMessage
import com.doneit.ascend.presentation.utils.*
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.timerTask

@CreateFactory
@ViewModelDiModule
class NewPasswordViewModel(
    private val userUseCase: UserUseCase,
    private val router: NewPasswordContract.Router
) : BaseViewModelImpl(), NewPasswordContract.ViewModel {

    override val newPasswordModel = PresentationNewPasswordModel()
    override val canSave = MutableLiveData<Boolean>()
    override val canResendCode = MutableLiveData<Boolean>(true)
    override val canShowTimer = MutableLiveData<Boolean>(false)
    override val timerValue = MutableLiveData<String>()
    override var sendTimer: Timer? = null
    override var end: Long = 0
    override val phoneNumber = MutableLiveData<String>("")

    init {
        newPasswordModel.code.validator = { s ->
            val result = ValidationResult()

            if (s.isValidCodeFromSms().not()) {
                result.isSussed = false
            }

            result
        }

        newPasswordModel.password.validator = { s ->
            val result = ValidationResult()

            if (s.isValidPassword().not()) {
                result.isSussed = false
                result.errors.add(R.string.error_password)
            }

            result
        }

        newPasswordModel.passwordConfirmation.validator = { s ->
            val result = ValidationResult()

            if (s != newPasswordModel.password.observableField.getNotNullString()) {
                result.isSussed = false
                result.errors.add(R.string.error_password_confirm)
            }

            result
        }

        val invalidationListener = { updateCanSave() }
        newPasswordModel.code.onFieldInvalidate = invalidationListener
        newPasswordModel.password.onFieldInvalidate = invalidationListener
        newPasswordModel.passwordConfirmation.onFieldInvalidate = invalidationListener
    }

    override fun applyArguments(args: NewPasswordArgs) {
        newPasswordModel.phoneNumber = args.phone
        phoneNumber.postValue(args.phone)
        showCodeSentMessage()
    }

    override fun removeErrors() {
        newPasswordModel.code.removeError()
        newPasswordModel.password.removeError()
        newPasswordModel.passwordConfirmation.removeError()
    }

    override fun saveClick() {
        canSave.postValue(false)
        viewModelScope.launch {
            val requestEntity = userUseCase.resetPassword(newPasswordModel.toEntity())
            canSave.postValue(true)

            if (requestEntity.isSuccessful) {
                router.navigateToLogInFragment()
            } else {
                if (requestEntity.errorModel!!.isNotEmpty()) {
                    errorMessage.call(
                        PresentationMessage(
                            Messages.DEFAULT_ERROR.getId(),
                            null,
                            requestEntity.errorModel!!.first()
                        )
                    )
                }
            }
        }
    }

    override fun resendCodeClick() {
        canSave.postValue(false)
        canResendCode.postValue(false)

        startTimer()

        viewModelScope.launch {
            val requestEntity = userUseCase.forgotPassword(newPasswordModel.phoneNumber)
            canSave.postValue(true)

            if (requestEntity.isSuccessful) {
                launch(Dispatchers.Main) {
                    showCodeSentMessage()
                }
            } else {
                if (requestEntity.errorModel!!.isNotEmpty()) {
                    errorMessage.call(
                        PresentationMessage(
                            Messages.DEFAULT_ERROR.getId(),
                            null,
                            requestEntity.errorModel!!.first()
                        )
                    )
                }
            }

            delay(Constants.RESEND_CODE_INTERVAL)
            canResendCode.postValue(true)
        }
    }

    override fun onBackClick() {
        newPasswordModel.clear()
        router.onBack()
    }

    private fun showCodeSentMessage() {
        successMessage.call(
            PresentationMessage(
                Messages.PASSWORD_SENT.getId()
            )
        )
    }

    private fun updateCanSave() {
        var isFormValid = true

        isFormValid = isFormValid and newPasswordModel.code.isValid
        isFormValid = isFormValid and newPasswordModel.password.isValid
        isFormValid = isFormValid and newPasswordModel.passwordConfirmation.isValid

        canSave.postValue(isFormValid)
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
}