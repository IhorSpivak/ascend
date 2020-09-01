package com.doneit.ascend.presentation.login

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.doneit.ascend.presentation.login.first_time_login.FirstTimeLoginContract
import com.doneit.ascend.presentation.login.first_time_login.FirstTimeLoginFragment
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
import com.doneit.ascend.presentation.utils.Constants.RC_SIGN_IN
import com.doneit.ascend.presentation.utils.extensions.replace
import com.doneit.ascend.presentation.utils.extensions.replaceWithBackStack
import com.doneit.ascend.presentation.web_page.WebPageContract
import com.doneit.ascend.presentation.web_page.WebPageFragment
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

    override fun onBack() {
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

    override fun navigateToTerms() {
        activity.supportFragmentManager.replaceWithBackStack(
            R.id.container, WebPageFragment.newInstance(
                R.string.terms_and_conditions,
                "terms_and_conditions"
            )
        )
    }

    override fun navigateToPrivacyPolicy() {
        activity.supportFragmentManager.replaceWithBackStack(
            R.id.container, WebPageFragment.newInstance(
                R.string.privacy,
                "privacy_policy"
            )
        )
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


    //TODO: refactor two methods in one:
    override fun goToMain(bundle: Bundle) {
        outerRouter.goToMain(bundle)
    }

    private fun closeKeyboard() {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun goToMain() {
        closeKeyboard()
        outerRouter.goToMain(Bundle())
    }

    override fun navigateToGoogleLogin() {
        val client = LoginHelper.getGoogleSignInClient(activity)

        val signInIntent = client.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun navigateToFirstTimeLogin() {
        activity.supportFragmentManager.replace(R.id.container, FirstTimeLoginFragment())
    }

}