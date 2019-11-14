package com.doneit.ascend.presentation.login.sign_up

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import com.vrgsoft.core.presentation.fragment.BaseViewModel

class SignUpViewModel(

) {

    //val registrationModel = Registration()

    val registrationEvent =  MutableLiveData<Boolean>()

    @SuppressLint("CheckResult")
    fun registrationClick() {
        /*signInUpProvider.registration(registrationModel)
            .doOnSubscribe(::disposeOnDestroy)
            .subscribe {
                registrationEvent.postValue(true)
            }*/
    }

}