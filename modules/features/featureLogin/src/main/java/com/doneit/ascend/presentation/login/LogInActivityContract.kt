package com.doneit.ascend.presentation.login

import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface LogInActivityContract {
    interface ViewModel: BaseViewModel {
        fun tryToLogin()
    }

    interface Router {
        fun goToMain()
        fun navigateToLogInFragment()
        fun navigateToFirstTimeLogin(questions: List<QuestionEntity>)
    }
}