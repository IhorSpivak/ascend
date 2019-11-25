package com.doneit.ascend.presentation.login.log_in

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.entity.LogInUserModel
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.R
import com.doneit.ascend.presentation.login.models.PresentationLoginModel
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class LogInViewModel(
    private val router: LogInContract.Router,
    private val userUseCase: UserUseCase
) : BaseViewModelImpl(), LogInContract.ViewModel {

    override val loginModel = PresentationLoginModel()
    override val isSignInEnabled = ObservableField<Boolean>(true)
    override val errorRes = MutableLiveData<Int?>()

    override fun singInClick() {
        viewModelScope.launch {
            isSignInEnabled.set(false)
            val requestEntity = userUseCase.signIn(LogInUserModel(loginModel.getPhoneNumber(), loginModel.password))

            if(requestEntity.isSuccessful){
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
}
