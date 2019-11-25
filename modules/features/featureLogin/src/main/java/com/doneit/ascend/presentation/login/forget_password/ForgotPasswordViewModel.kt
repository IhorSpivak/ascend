package com.doneit.ascend.presentation.login.forget_password

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.models.PresentationPhoneModel
import com.doneit.ascend.presentation.login.models.ValidationResult
import com.doneit.ascend.presentation.login.models.toEntity
import com.doneit.ascend.presentation.login.utils.getNotNull
import com.doneit.ascend.presentation.login.utils.isPhoneValid
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.main.models.PresentationMessage
import com.doneit.ascend.presentation.utils.Messages
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class ForgotPasswordViewModel(
    private val router: ForgotPasswordContract.Router,
    private val userUseCase: UserUseCase
) : BaseViewModelImpl(), ForgotPasswordContract.ViewModel {

    override val phoneModel = PresentationPhoneModel()
    override val canContinue = MutableLiveData<Boolean>()

    init {
        phoneModel.phone.validator = { s ->
            val result = ValidationResult()
            if(isPhoneValid(phoneModel.phoneCode.getNotNull(), phoneModel.phone.observableField.getNotNull()).not()) {
                result.isSussed = false
                result.errors.add(R.string.error_phone)
            }
            result
        }

        phoneModel.phone.onFieldInvalidate = {
            canContinue.postValue(phoneModel.phone.isValid)
        }
    }

    override fun resetClick() {
        canContinue.postValue(false)
        viewModelScope.launch {
            val requestEntity = userUseCase.forgotPassword(phoneModel.toEntity())
            canContinue.postValue(true)

            if(requestEntity.isSuccessful){
                launch(Dispatchers.Main) {
                    successMessage.postValue(PresentationMessage(
                        Messages.PASSWORD_SENT.getId()
                    ))
                }
            } else {
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

    override fun onBackClick() {
        router.goBack()
    }
}
