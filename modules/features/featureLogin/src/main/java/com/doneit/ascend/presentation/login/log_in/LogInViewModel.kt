package com.doneit.ascend.presentation.login.log_in

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.LogInUserModel
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.models.PresentationLoginModel
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.core.presentation.fragment.BaseViewModelImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class LogInViewModel(
    private val router: LogInContract.Router,
    private val userUseCase: UserUseCase
) : BaseViewModelImpl(), LogInContract.ViewModel {

    override val loginModel = PresentationLoginModel()
    override val isSignInEnabled = ObservableField<Boolean>(true)
    override val errorMessage = MutableLiveData<String>()

    override fun singInClick() {
        GlobalScope.launch {
            isSignInEnabled.set(false)
            val requestEntity = userUseCase.signIn(LogInUserModel(loginModel.phoneCode+loginModel.phone, loginModel.password))

            if(requestEntity.isSuccessful){
                launch(Dispatchers.Main) {
                    router.goToMain()
                }
            } else {
                requestEntity.errorModel?.forEach {
                    errorMessage.postValue(it)
                }
            }
            isSignInEnabled.set(true)
        }
    }

    override fun signUpClick() {
        router.navigateToSignUp()
    }
}
