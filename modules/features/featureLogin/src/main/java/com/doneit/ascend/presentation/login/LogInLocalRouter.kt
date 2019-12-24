package com.doneit.ascend.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.doneit.ascend.domain.entity.QuestionListEntity
import com.doneit.ascend.presentation.login.first_time_login.FirstTimeLoginContract
import com.doneit.ascend.presentation.login.first_time_login.FirstTimeLoginFragment
import com.doneit.ascend.presentation.login.first_time_login.common.FirstTimeLoginArgs
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
import com.doneit.ascend.presentation.login.utils.LoginHelper
import com.doneit.ascend.presentation.web_page.WebPageContract
import com.doneit.ascend.presentation.web_page.WebPageFragment
import com.doneit.ascend.presentation.web_page.common.WebPageArgs
import com.doneit.ascend.presentation.main.extensions.replace
import com.doneit.ascend.presentation.main.extensions.replaceWithBackStack
import com.doneit.ascend.presentation.utils.Constants.RC_SIGN_IN
import com.vrgsoft.core.presentation.fragment.argumented.ArgumentedFragment

class LogInLocalRouter(
    private val activity: LogInActivity,
    private val outerRouter: ILogInAppRouter
) : LogInActivityContract.Router,
    LogInContract.Router,
    SignUpContract.Router,
    ForgotPasswordContract.Router,
    NewPasswordContract.Router,
    FirstTimeLoginContract.Router,
    WebPageContract.Router {

    override fun goBack() {
        activity.supportFragmentManager.popBackStack()
    }

    override fun goBackToLogin() {
        //todo
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

    override fun navigateToTerms() {
        val args = WebPageArgs("Terms & Conditions", "terms_and_conditions")

        val fragment = WebPageFragment()
        (fragment as Fragment).arguments = Bundle().apply {
            putParcelable(ArgumentedFragment.KEY_ARGS, args)
        }

        activity.supportFragmentManager.replaceWithBackStack(R.id.container, fragment)
    }

    override fun navigateToPrivacyPolicy() {
        val args = WebPageArgs("Privacy Policy", "privacy_policy")

        val fragment = WebPageFragment()
        (fragment as Fragment).arguments = Bundle().apply {
            putParcelable(ArgumentedFragment.KEY_ARGS, args)
        }

        activity.supportFragmentManager.replaceWithBackStack(R.id.container, fragment)
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
        activity.supportFragmentManager.replaceWithBackStack(
            R.id.container,
            ForgotPasswordFragment()
        )
    }

    override fun goToMain() {
        outerRouter.goToMain()
    }

    override fun navigateToGoogleLogin() {
        val client = LoginHelper.getGoogleSignInClient(activity)

        val signInIntent = client.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun navigateToFirstTimeLogin(questions: QuestionListEntity) {

        val args = FirstTimeLoginArgs(questions)

        val fragment = FirstTimeLoginFragment()
        (fragment as Fragment).arguments = Bundle().apply {
            putParcelable(ArgumentedFragment.KEY_ARGS, args)
        }

        activity.supportFragmentManager.replace(R.id.container, fragment)
    }
}