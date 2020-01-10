package com.doneit.ascend.presentation.profile.change_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.PresentationChangePasswordModel
import com.doneit.ascend.presentation.models.ValidationResult
import com.doneit.ascend.presentation.models.toEntity
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.doneit.ascend.presentation.utils.isValidPassword
import com.doneit.ascend.presentation.utils.getNotNull
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class ChangePasswordViewModel (
    private val router: ChangePasswordContract.Router,
    private val userUseCase: UserUseCase
): BaseViewModelImpl(), ChangePasswordContract.ViewModel {

    override val dataModel = PresentationChangePasswordModel()
    override val canSave = MutableLiveData<Boolean>(false)

    init {
        dataModel.currentPassword.validator = { s ->
            val result = ValidationResult()

            if (s.isValidPassword().not()) {
                result.isSucceed = false
                result.errors.add(R.string.error_password)
            }

            result
        }

        dataModel.newPassword.validator = { s ->
            val result = ValidationResult()

            if (s.isValidPassword().not()) {
                result.isSucceed = false
                result.errors.add(R.string.error_password)
            }

            result
        }

        dataModel.confirmPassword.validator = { s ->
            val result = ValidationResult()

            if (s != dataModel.newPassword.observableField.getNotNull()) {
                result.isSucceed = false
                result.errors.add(R.string.error_password_confirm)
            }

            result
        }

        val invalidationListener = { updateSaveState() }
        dataModel.currentPassword.onFieldInvalidate = invalidationListener
        dataModel.newPassword.onFieldInvalidate = invalidationListener
        dataModel.confirmPassword.onFieldInvalidate = invalidationListener
    }

    override fun onSaveClick() {
        canSave.postValue(false)
        viewModelScope.launch {
            val result = userUseCase.changePassword(dataModel.toEntity())

            if(result.isSuccessful) {
                router.onBack()
            } else {
                showDefaultErrorMessage(result.errorModel!!.toErrorMessage())
            }
            canSave.postValue(true)
        }
    }

    override fun onBackClick() {
        router.onBack()
    }

    private fun updateSaveState() {
        var isFormValid = true

        isFormValid = isFormValid and dataModel.newPassword.isValid
        isFormValid = isFormValid and dataModel.confirmPassword.isValid

        canSave.postValue(isFormValid)
    }
}