package com.doneit.ascend.presentation.login

import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.LoginUserModel
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.models.PresentationLoginModel
import com.vrgsoft.core.presentation.fragment.BaseViewModelImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LogInViewModel(
    private val router: LogInContract.Router,
    private val userUseCase: UserUseCase
) : BaseViewModelImpl(), LogInContract.ViewModel {

    override val loginModel = PresentationLoginModel()
    override val isSignInEnabled = ObservableField<Boolean>(true)
    override val errorMessage = MutableLiveData<Int>()

    override fun singInClick() {
        GlobalScope.launch {
            isSignInEnabled.set(false)
            val isSuccess = userUseCase.login(LoginUserModel(loginModel.phoneCode+loginModel.phone, loginModel.password))
            if(isSuccess){
                launch(Dispatchers.Main) {
                    router.goToMain()
                }
            } else {
                //todo replace by correct error handling
                errorMessage.postValue(R.string.login_error)
            }
            isSignInEnabled.set(true)
        }
    }
}
