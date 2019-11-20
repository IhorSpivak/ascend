package com.doneit.ascend.presentation.login

import com.doneit.ascend.presentation.login.log_in.LogInContract
import com.doneit.ascend.presentation.login.log_in.LogInFragment
import com.doneit.ascend.presentation.login.sign_up.SignUpContract
import com.doneit.ascend.presentation.login.sign_up.SignUpFragment
import com.doneit.ascend.presentation.login.sign_up.verify_phone.VerifyPhoneFragment
import com.doneit.ascend.presentation.main.extensions.replace
import com.doneit.ascend.presentation.main.extensions.replaceWithBackStack

class LogInLocalRouter(
    private val activity: LogInActivity,
    private val outerRouter: ILogInRouter
) : LogInContract.Router,
    SignUpContract.Router {

    override fun goBack() {
        activity.supportFragmentManager.popBackStack()
    }

    fun navigateToLogInFragment() {
        activity.supportFragmentManager.replace(R.id.container, LogInFragment())
    }

    override fun navigateToVerifyPhone() {
        activity.supportFragmentManager.replaceWithBackStack(R.id.container, VerifyPhoneFragment())
    }

    override fun navigateToSignUp() {
        activity.supportFragmentManager.replaceWithBackStack(R.id.container, SignUpFragment())
    }

    override fun goToMain() {
        outerRouter.goToMain()
    }
}