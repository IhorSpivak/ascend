package com.doneit.ascend.presentation.login

import com.doneit.ascend.presentation.login.log_in.LogInContract
import com.doneit.ascend.presentation.login.log_in.LogInFragment
import com.doneit.ascend.presentation.main.extensions.replace

class LogInLocalRouter (
    private val activity: LogInActivity,
    private val outerRouter: ILogInRouter
): LogInContract.Router{

    fun navigateToLogInFragment() {
        activity.supportFragmentManager.replace(R.id.container, LogInFragment())
    }

    override fun navigateToSignUp() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun goToMain() {
        outerRouter.goToMain()
    }
}