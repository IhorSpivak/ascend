package com.doneit.ascend.presentation.login

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.models.PresentationLoginModel
import com.vrgsoft.core.presentation.fragment.BaseViewModelImpl

class LogInViewModel(
    private val router: LogInContract.Router,
    private val userUseCase: UserUseCase
) : BaseViewModelImpl(), LogInContract.ViewModel {

    override val loginModel = PresentationLoginModel("23")

    val loginEvent = MutableLiveData<Boolean>()

    @SuppressLint("CheckResult")
    override fun singInClick() {
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
