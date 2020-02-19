package com.doneit.ascend.presentation.login

import android.os.Bundle
import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import kotlinx.coroutines.launch

class LogInActivityViewModel(
    private val router: LogInActivityContract.Router,
    private val userUseCase: UserUseCase
) : BaseViewModelImpl(), LogInActivityContract.ViewModel {

    override fun tryToLogin(bundle: Bundle) {
        viewModelScope.launch {
            if (userUseCase.hasSignedInUser()) {
                val userLocal = userUseCase.getUser()
                if (userLocal?.unansweredQuestionsCount == 0) {
                    router.goToMain(bundle)
                } else {
                    router.navigateToFirstTimeLogin()
                }
            } else {
                router.navigateToLogInFragment()
            }
        }
    }
}