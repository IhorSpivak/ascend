package com.doneit.ascend.presentation.profile.edit_email

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.EditEmailModel
import com.doneit.ascend.presentation.models.ValidationResult
import com.doneit.ascend.presentation.models.toEntity
import com.doneit.ascend.presentation.utils.extensions.toErrorMessage
import com.doneit.ascend.presentation.utils.isValidEmail
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class EditEmailViewModel(
    private val router: EditEmailContract.Router,
    private val userUseCase: UserUseCase
) : BaseViewModelImpl(), EditEmailContract.ViewModel {

    override val dataModel = EditEmailModel()
    override val canSave = MutableLiveData<Boolean>()

    init {
        dataModel.email.validator = { s ->
            val result = ValidationResult()
            if (s.isValidEmail().not()) {
                result.isSucceed = false
                result.errors.add(R.string.error_email)
            }
            result
        }

        dataModel.password.validator = { s ->
            val result = ValidationResult()
            if (s.isBlank()) {
                result.isSucceed = false
                result.errors.add(R.string.error_email)
            }
            result
        }

        val invalidationListener = { updateSaveState() }
        dataModel.email.onFieldInvalidate = invalidationListener
        dataModel.password.onFieldInvalidate = invalidationListener
    }

    override fun onSaveClick() {
        canSave.postValue(false)
        viewModelScope.launch {
            val result = userUseCase.changeEmail(dataModel.toEntity())

            if (result.isSuccessful) {
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

        isFormValid = isFormValid and dataModel.email.isValid
        isFormValid = isFormValid and dataModel.password.isValid

        canSave.postValue(isFormValid)
    }
}