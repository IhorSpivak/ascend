package com.doneit.ascend.presentation.login.new_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.models.PresentationNewPasswordModel
import com.doneit.ascend.presentation.login.models.ValidationResult
import com.doneit.ascend.presentation.login.models.toEntity
import com.doneit.ascend.presentation.login.new_password.common.NewPasswordArgs
import com.doneit.ascend.presentation.login.utils.getNotNull
import com.doneit.ascend.presentation.login.utils.isValidConfirmationCode
import com.doneit.ascend.presentation.login.utils.isValidPassword
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.models.PresentationMessage
import com.doneit.ascend.presentation.utils.Constants
import com.doneit.ascend.presentation.utils.Messages
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class NewPasswordViewModel(
    private val userUseCase: UserUseCase,
    private val router: NewPasswordContract.Router
) : BaseViewModelImpl(), NewPasswordContract.ViewModel {

    override val newPasswordModel = PresentationNewPasswordModel()
    override val canSave = MutableLiveData<Boolean>()
    override val canResendCode = MutableLiveData<Boolean>(true)

    init {
        newPasswordModel.code.validator = { s ->
            val result = ValidationResult()

            if (s.isValidConfirmationCode().not()) {
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

            if (s != newPasswordModel.password.observableField.getNotNull()) {
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
                launch(Dispatchers.Main) {
                    router.goToMain()
                }
            } else {
                if(requestEntity.errorModel!!.isNotEmpty()) {
                    errorMessage.postValue(
                        PresentationMessage(
                            Messages.EROR.getId(),
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

        viewModelScope.launch {
            val requestEntity = userUseCase.forgotPassword(newPasswordModel.phoneNumber)
            canSave.postValue(true)

            if (requestEntity.isSuccessful) {
                launch(Dispatchers.Main) {
                    showCodeSentMessage()
                }
            } else {
                if(requestEntity.errorModel!!.isNotEmpty()){
                    errorMessage.postValue(
                        PresentationMessage(
                            Messages.EROR.getId(),
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
        router.goBack()
    }

    private fun showCodeSentMessage() {
        successMessage.postValue(
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
}