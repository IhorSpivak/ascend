package com.doneit.ascend.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.doneit.ascend.presentation.login.forget_password.ForgotPasswordContract
import com.doneit.ascend.presentation.login.forget_password.ForgotPasswordFragment
import com.doneit.ascend.presentation.login.log_in.LogInContract
import com.doneit.ascend.presentation.login.log_in.LogInFragment
import com.doneit.ascend.presentation.login.new_password.NewPasswordContract
import com.doneit.ascend.presentation.login.new_password.NewPasswordFragment
import com.doneit.ascend.presentation.login.new_password.common.NewPasswordArgs
import com.doneit.ascend.presentation.login.sign_up.SignUpContract
import com.doneit.ascend.presentation.login.sign_up.SignUpFragment
import com.doneit.ascend.presentation.login.sign_up.verify_phone.VerifyPhoneFragment
import com.doneit.ascend.presentation.main.extensions.replace
import com.doneit.ascend.presentation.main.extensions.replaceWithBackStack
import com.vrgsoft.core.presentation.fragment.argumented.ArgumentedFragment

class LogInLocalRouter(
    private val activity: LogInActivity,
    private val outerRouter: ILogInRouter
) : LogInContract.Router,
    SignUpContract.Router,
    ForgotPasswordContract.Router,
    NewPasswordContract.Router {

    override fun goBack() {
        activity.supportFragmentManager.popBackStack()
    }

    override fun navigateToLogInFragment() {
        activity.supportFragmentManager.replace(R.id.container, LogInFragment())
    }

    override fun navigateToVerifyPhone() {
        activity.supportFragmentManager.replaceWithBackStack(R.id.container, VerifyPhoneFragment())
    }

    override fun navigateToSignUp() {
        activity.supportFragmentManager.replaceWithBackStack(R.id.container, SignUpFragment())
    }

    override fun navigateToNewPassword(phone: String) {

        val args = NewPasswordArgs(phone)

        val fragment = NewPasswordFragment()
        (fragment as Fragment).arguments = Bundle().apply {
            putParcelable(ArgumentedFragment.KEY_ARGS, args)
        }

        activity.supportFragmentManager.replaceWithBackStack(R.id.container, fragment)
    }

    override fun navigateToForgotPassword() {
        activity.supportFragmentManager.replaceWithBackStack(R.id.container, ForgotPasswordFragment())
    }

    override fun goToMain() {
        outerRouter.goToMain()
    }
}