package com.doneit.ascend.ui.signup

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.doneit.ascend.Injector
import com.doneit.ascend.providers.SignInUpProvider
import com.doneit.ascend.ui.BaseViewModel
import javax.inject.Inject

class SignUpViewModel : BaseViewModel() {

    @Inject
    lateinit var signInUpProvider: SignInUpProvider

    init {
        Injector.appComponent.inject(this)
    }

    val registrationModel = Registration()

    val registrationEvent =  MutableLiveData<Boolean>()

    @SuppressLint("CheckResult")
    fun registrationClick() {
        signInUpProvider.registration(registrationModel)
            .doOnSubscribe(::disposeOnDestroy)
            .subscribe {
                registrationEvent.postValue(true)
            }
    }

}
