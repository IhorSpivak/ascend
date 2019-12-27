package com.doneit.ascend.presentation.login

import androidx.lifecycle.viewModelScope
import com.doneit.ascend.domain.use_case.interactor.question.QuestionUseCase
import com.doneit.ascend.domain.use_case.interactor.user.UserUseCase
import com.doneit.ascend.presentation.main.base.BaseViewModelImpl
import com.doneit.ascend.presentation.utils.LocalStorage
import com.doneit.ascend.presentation.utils.UIReturnStep
import kotlinx.coroutines.launch

class LogInActivityViewModel (
    private val router: LogInActivityContract.Router,
    private val localStorage: LocalStorage,
    private val questionUseCase: QuestionUseCase,
    private val userUseCase: UserUseCase
): BaseViewModelImpl(), LogInActivityContract.ViewModel {

    override fun tryToLogin() {
        viewModelScope.launch {
            if(userUseCase.hasSignedInUser()) {
                val step = localStorage.getUIReturnStep()
                when (step) {
                    UIReturnStep.FIRST_TIME_LOGIN -> {
                        val questions = questionUseCase.getQuestionsList()
                        router.navigateToFirstTimeLogin(questions)
                    }
                    else -> {
                        router.goToMain()
                    }
                }
            } else {
                router.navigateToLogInFragment()
            }
        }
    }


}