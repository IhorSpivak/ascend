package com.doneit.ascend.presentation.profile.edit_phone

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.getDefaultCalendar
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.EditPhoneModel
import com.doneit.ascend.presentation.models.ValidationResult
import com.doneit.ascend.presentation.models.toEntity
import com.doneit.ascend.presentation.profile.edit_phone.verify_phone.VerifyChangePhoneContract
import com.doneit.ascend.presentation.utils.*
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.doneit.ascend.presentation.utils.extensions.toTimerFormat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.timerTask


class EditPhoneViewModel(
    private val router: EditPhoneContract.Router,
    private val userUseCase: UserUseCase
) : BaseViewModelImpl(), EditPhoneContract.ViewModel, VerifyChangePhoneContract.ViewModel {

    override val dataModel = EditPhoneModel()
    override val canContinue = MutableLiveData<Boolean>(false)
    override val canVerify = MutableLiveData<Boolean>(false)
    override val canResendCode = MutableLiveData<Boolean>(true)
    override val timerValue = MutableLiveData<String>()

    private var sendTimer = Timer()

    init {
        dataModel.phoneNumber.validator = { _ ->
            val result = ValidationResult()
            if (isPhoneValid(
                    dataModel.phoneCode.getNotNull(),
                    dataModel.phoneNumber.observableField.getNotNull()
                ).not()
            ) {
                result.isSucceed = false
                result.errors.add(R.string.error_phone)
            }
            result
        }

        dataModel.code.validator = { s ->
            val result = ValidationResult()

            if (s.isValidConfirmationCode().not()) {
                result.isSucceed = false
            }

            result
        }

        dataModel.phoneNumber.onFieldInvalidate = { canContinue.postValue(dataModel.phoneNumber.isValid) }
        dataModel.code.onFieldInvalidate = { canVerify.postValue(dataModel.code.isValid) }
    }

    override fun init() {
        if(dataModel.phoneCode.getNotNull().isBlank() || dataModel.phoneNumber.observableField.getNotNull().isBlank()) {
            viewModelScope.launch {
                val user = userUseCase.getUser()
                val code = user!!.phone!!.getCountyCode()
                val phone = user.phone!!.getPhoneBody()
                dataModel.phoneCode.set(code)
            }
        }
    }

    override fun continueClick() {
        router.navigateToVerifyPhone()
    }

    override fun sendCode(shouldBlock: Boolean) {
        viewModelScope.launch {
            if(shouldBlock) {
                canResendCode.postValue(false)
            }

            val result = userUseCase.getConfirmationCode(dataModel.getPhone())

            if (result.isSuccessful.not()) {
                if (result.errorModel!!.isNotEmpty()) {
                    showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
                }
            }

            if(shouldBlock) {
                startTimer()
                delay(Constants.RESEND_CODE_INTERVAL)
                canResendCode.postValue(true)
                sendTimer.cancel()
            }
        }
    }

    override fun onVerifyClick() {
        canVerify.postValue(false)
        viewModelScope.launch {
            val result = userUseCase.changePhone(dataModel.toEntity())

            if (result.isSuccessful) {
                router.onBack()
                router.onBack()
            } else {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
            dataModel.code.invalidate()
        }
    }

    override fun onBackClick() {
        router.onBack()
    }

    private fun startTimer() {
        val calendar = getDefaultCalendar()
        calendar.add(Calendar.MILLISECOND, Constants.RESEND_CODE_INTERVAL.toInt())

        sendTimer.schedule(timerTask {
            val currentTime = Date().time
            val diff = calendar.timeInMillis - currentTime

            if (diff <= 0) {
                return@timerTask
            } else {
                timerValue.postValue(Date(diff).toTimerFormat())
            }
        }, 0, 1000)
    }
}