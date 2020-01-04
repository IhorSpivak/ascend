package com.doneit.ascend.presentation.profile.edit_phone

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.models.EditPhoneModel
import com.doneit.ascend.presentation.models.ValidationResult
import com.doneit.ascend.presentation.utils.getCountyCode
import com.doneit.ascend.presentation.utils.getNotNull
import com.doneit.ascend.presentation.utils.getPhoneBody
import com.doneit.ascend.presentation.utils.isPhoneValid
import kotlinx.coroutines.launch


class EditPhoneViewModel (
    private val router: EditPhoneContract.Router,
    private val userUseCase: UserUseCase
): BaseViewModelImpl(), EditPhoneContract.ViewModel {

    override val dataModel = EditPhoneModel()
    override val canContinue = MutableLiveData<Boolean>(false)

    init {
        dataModel.phoneNumber.validator = { _ ->
            val result = ValidationResult()
            if (isPhoneValid(
                    dataModel.phoneCode.getNotNull(),
                    dataModel.phoneNumber.observableField.getNotNull()
                ).not()
            ) {
                result.isSussed = false
                result.errors.add(R.string.error_phone)
            }
            result
        }

        val invalidationListener = { updateIsCountinueEnabled() }
        dataModel.phoneNumber.onFieldInvalidate = invalidationListener
    }

    override fun init() {
        viewModelScope.launch {
            val user = userUseCase.getUser()
            val code = user!!.phone!!.getCountyCode()
            val phone = user.phone!!.getPhoneBody()
            dataModel.phoneCode.set(code)
            dataModel.phoneNumber.observableField.set(phone)
        }
    }

    override fun continueClick() {
        router.navigateToVerifyPhone()
    }

    override fun onBackClick() {
        router.onBack()
    }

    private fun updateIsCountinueEnabled() {
        var isFormValid = true

        isFormValid = isFormValid and dataModel.phoneNumber.isValid

        canContinue.postValue(isFormValid)
    }
}