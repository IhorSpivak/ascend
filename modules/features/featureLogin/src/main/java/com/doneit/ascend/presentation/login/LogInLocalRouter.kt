package com.doneit.ascend.presentation.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.doneit.ascend.domain.entity.AnswerOptionEntity
import com.doneit.ascend.domain.entity.QuestionEntity
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
import com.doneit.ascend.presentation.main.extensions.replace
import com.doneit.ascend.presentation.main.extensions.replaceWithBackStack
import com.vrgsoft.core.presentation.fragment.argumented.ArgumentedFragment

class LogInLocalRouter(
    private val activity: LogInActivity,
    private val outerRouter: ILogInRouter
) : LogInContract.Router,
    SignUpContract.Router,
    ForgotPasswordContract.Router,
    NewPasswordContract.Router,
    FirstTimeLoginContract.Router {

    override fun goBack() {
        activity.supportFragmentManager.popBackStack()
    }

    override fun goBackToLogin() {
        //todo
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

    override fun navigateToFirstTimeLogin() {

        val questions = listOf(
            QuestionEntity(
                1,
                "How did you hear about us?",
                "question",
                "2019-11-10T09:24:23.115Z",
                "2019-11-10T09:24:23.115Z",
                null
            ),
            QuestionEntity(
                2,
                "MasterMind following",
                "question",
                "2019-11-10T09:24:23.247Z",
                "2019-11-10T09:24:23.247Z",
                null
            ),
            QuestionEntity(
                3,
                "Chose a community",
                "select_answer",
                "2019-11-10T09:24:23.391Z",
                "2019-11-10T09:24:23.391Z",
                listOf(
                    AnswerOptionEntity(
                        1,
                        "Recovery",
                        0,
                        "2019-11-10T09:24:23.392Z",
                        "2019-11-10T09:24:23.392Z"
                    ),
                    AnswerOptionEntity(
                        2,
                        "Family",
                        1,
                        "2019-11-10T09:24:23.393Z",
                        "2019-11-10T09:24:23.393Z"
                    ),
                    AnswerOptionEntity(
                        3,
                        "Spiritual",
                        3,
                        "2019-11-10T09:24:23.393Z",
                        "2019-11-10T09:24:23.393Z"
                    ),
                    AnswerOptionEntity(
                        4,
                        "Leadership",
                        4,
                        "2019-11-10T09:24:23.393Z",
                        "2019-11-10T09:24:23.393Z"
                    )
                )
            )
        )

        val args = FirstTimeLoginArgs(questions)

        val fragment = FirstTimeLoginFragment()
        (fragment as Fragment).arguments = Bundle().apply {
            putParcelable(ArgumentedFragment.KEY_ARGS, args)
        }

        activity.supportFragmentManager.replaceWithBackStack(R.id.container, fragment)
    }
}