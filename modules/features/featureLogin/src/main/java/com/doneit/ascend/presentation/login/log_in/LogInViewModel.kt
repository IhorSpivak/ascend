package com.doneit.ascend.presentation.login.log_in

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.LogInUserModel
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.models.PresentationLoginModel
import com.doneit.ascend.presentation.login.models.ValidationResult
import com.doneit.ascend.presentation.login.utils.getNotNull
import com.doneit.ascend.presentation.login.utils.isPhoneValid
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.LocalStorage
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class LogInViewModel(
    private val router: LogInContract.Router,
    private val userUseCase: UserUseCase,
    private val localStorage: LocalStorage
) : BaseViewModelImpl(), LogInContract.ViewModel {

    override val loginModel = PresentationLoginModel()
    override val isSignInEnabled = ObservableField<Boolean>(true)
    override val errorRes = MutableLiveData<Int?>()

    init {
        loginModel.phone.validator = { s ->
            val result = ValidationResult()
            if (isPhoneValid(
                    loginModel.phoneCode.getNotNull(),
                    loginModel.phone.observableField.getNotNull()
                ).not()
            ) {
                result.isSussed = false
                result.errors.add(R.string.error_phone)
            }
            result
        }

        val invalidationListener = { updateIsSignInEnabled() }
        loginModel.phone.onFieldInvalidate = invalidationListener
    }

    override fun singInClick() {
        viewModelScope.launch {
            isSignInEnabled.set(false)
            val requestEntity =
                userUseCase.signIn(
                    LogInUserModel(
                        loginModel.getPhoneNumber(),
                        loginModel.password
                    )
                )

            if (requestEntity.isSuccessful) {

                val token = requestEntity.successModel?.token
                token?.let {
                    localStorage.saveSessionToken(it)
                }

                launch(Dispatchers.Main) {
                    router.goToMain()
                }
            } else {
                errorRes.postValue(R.string.error_login)
            }
            isSignInEnabled.set(true)
        }
    }

    override fun signUpClick() {
        router.navigateToSignUp()
    }

    override fun forgotPasswordClick() {
        router.navigateToForgotPassword()
    }

    private fun updateIsSignInEnabled() {
        var isFormValid = true

        isFormValid = isFormValid and loginModel.phone.isValid

        isSignInEnabled.set(isFormValid)
    }
}
