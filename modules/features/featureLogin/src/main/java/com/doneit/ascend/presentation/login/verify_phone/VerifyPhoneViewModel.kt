package com.doneit.ascend.presentation.login.verify_phone

import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.login.models.PresentationSignUpModel
import com.doneit.ascend.presentation.login.models.toEntity
import com.vrgsoft.annotations.CreateFactory
import com.vrgsoft.annotations.ViewModelDiModule
import com.vrgsoft.core.presentation.fragment.BaseViewModelImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@CreateFactory
@ViewModelDiModule
class VerifyPhoneViewModel (
    private val userUseCase: UserUseCase,
    private val router: VerifyPhoneContract.Router
): BaseViewModelImpl(), VerifyPhoneContract.ViewModel {

    private lateinit var signUpModel: PresentationSignUpModel

    override fun setModel(signUpModel: PresentationSignUpModel) {
        sendCode()
        this.signUpModel = signUpModel
    }

    override fun onVerifyClick() {
        signUpModel.code = "4242"//todo handle code
        GlobalScope.launch {
            val requestEntity = userUseCase.signUp(signUpModel.toEntity())

            if(requestEntity.isSuccessful){
                launch(Dispatchers.Main) {
                    router.goToMain()
                }
            }
        }
    }

    override fun sendCode() {
        GlobalScope.launch {
            userUseCase.getConfirmationCode(signUpModel.getPhoneNumber())
        }
    }

    override fun onBackClick() {
        router.goBack()
    }
}