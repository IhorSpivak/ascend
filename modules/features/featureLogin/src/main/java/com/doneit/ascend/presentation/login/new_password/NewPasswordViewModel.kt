package com.doneit.ascend.presentation.login.new_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.models.PresentationNewPasswordModel
import com.doneit.ascend.presentation.login.models.ValidationResult
import com.doneit.ascend.presentation.login.new_password.common.NewPasswordArgs
import com.doneit.ascend.presentation.login.utils.getNotNull
import com.doneit.ascend.presentation.login.utils.isValidPassword
import com.vrgsoft.core.presentation.fragment.BaseViewModelImpl
import com.vrgsoft.networkmanager.livedata.SingleLiveManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewPasswordViewModel(
    private val userUseCase: UserUseCase,
    private val router: NewPasswordContract.Router
) : BaseViewModelImpl(), NewPasswordContract.ViewModel {

    override val newPasswordModel = PresentationNewPasswordModel()
    override val canSave = MutableLiveData<Boolean>()
    override val showSuccessSendSMSMessage = SingleLiveManager(false)
    private val phone = MutableLiveData<String>()

    init {
        newPasswordModel.code.validator = { s ->
            val result = ValidationResult()

            // TODO: it is valid validation
            if (s.isNotEmpty()) {
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
        phone.postValue(args.phone)
    }

    override fun removeErrors() {
        newPasswordModel.code.removeError()
        newPasswordModel.password.removeError()
        newPasswordModel.passwordConfirmation.removeError()
    }

    override fun saveClick() {
        canSave.postValue(false)
    }

    override fun resendCodeClick() {
        canSave.postValue(false)

        viewModelScope.launch {
            val requestEntity = userUseCase.forgotPassword(phone.value ?: return@launch)
            canSave.postValue(true)

            if (requestEntity.isSuccessful) {
                launch(Dispatchers.Main) {
                    showSuccessSendSMSMessage.call(true)
                }
            }
        }
    }

    override fun onBackClick() {
        router.navigateToLogInFragment()
    }

    private fun updateCanSave() {
        var isFormValid = true

        isFormValid = isFormValid and newPasswordModel.code.isValid
        isFormValid = isFormValid and newPasswordModel.password.isValid
        isFormValid = isFormValid and newPasswordModel.passwordConfirmation.isValid

        canSave.postValue(isFormValid)
    }
}