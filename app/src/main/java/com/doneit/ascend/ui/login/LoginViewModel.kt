package com.doneit.ascend.ui.login

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.Injector
import com.doneit.ascend.providers.SignInUpProvider
import com.doneit.ascend.ui.BaseViewModel
import javax.inject.Inject

class Login(
    var phoneCode: String = "",
    var phone: String = "",
    var password: String = "",
    var valid: Boolean = true
)

class LoginViewModel : BaseViewModel() {

    @Inject
    lateinit var signInUpProvider: SignInUpProvider

    init {
        Injector.appComponent.inject(this)
    }

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
