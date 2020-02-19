package com.doneit.ascend.presentation.login

import android.os.Bundle
import com.doneit.ascend.domain.entity.QuestionListEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface LogInActivityContract {
    interface ViewModel : BaseViewModel {
        fun tryToLogin(bundle: Bundle = Bundle())
    }

    interface Router {
        fun goToMain(bundle: Bundle = Bundle())
        fun navigateToLogInFragment()
        fun navigateToFirstTimeLogin()
    }
}