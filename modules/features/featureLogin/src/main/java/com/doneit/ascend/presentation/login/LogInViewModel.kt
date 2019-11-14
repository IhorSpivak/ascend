package com.doneit.ascend.presentation.login

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.entity.LoginUserModel
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.models.Login
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.core.presentation.fragment.BaseViewModelImpl

class LogInViewModel(
    private val router: LogInContract.Router,
    private val userUseCase: UserUseCase
) : BaseViewModelImpl(), LogInContract.ViewModel {

    val loginModel = Login("23")

    val loginEvent = MutableLiveData<Boolean>()

    @SuppressLint("CheckResult")
    fun singInClick() {
//        signInUpProvider.logIn(
//            "${loginModel.phoneCode}${loginModel.phone}",
//            loginModel.password
//        )
//            .doOnSubscribe(::disposeOnDestroy)
//            .subscribe {
        loginEvent.postValue(true)
//            }
    }
}
