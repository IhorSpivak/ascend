package com.doneit.ascend.presentation.login.sign_up

import com.doneit.ascend.domain.entity.SignUpModel
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.core.presentation.fragment.BaseViewModelImpl

@CreateFactory
@ViewModelDiModule
class SignUpViewModel(

): BaseViewModelImpl(), SignUpContract.ViewModel {

    override val registrationModel = SignUpModel()

    override fun registrationClick() {
        /*signInUpProvider.registration(registrationModel)
            .doOnSubscribe(::disposeOnDestroy)
            .subscribe {
                registrationEvent.postValue(true)
            }*/
    }

}