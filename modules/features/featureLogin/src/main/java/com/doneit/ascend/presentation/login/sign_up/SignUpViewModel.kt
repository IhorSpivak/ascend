package com.doneit.ascend.presentation.login.sign_up

import com.doneit.ascend.presentation.login.models.PresentationSignUpModel
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.core.presentation.fragment.BaseViewModelImpl

@CreateFactory
@ViewModelDiModule
class SignUpViewModel(
    private val router: SignUpContract.Router
): BaseViewModelImpl(), SignUpContract.ViewModel {

    override val registrationModel = PresentationSignUpModel()

    override fun continueClick() {
        router.navigateToVerifyPhone(registrationModel)
    }

    override fun onBackClick() {
        router.goBack()
    }
}