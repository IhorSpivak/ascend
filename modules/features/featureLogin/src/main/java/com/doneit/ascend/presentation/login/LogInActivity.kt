package com.doneit.ascend.presentation.login

import android.content.Intent
import android.os.Bundle
import com.doneit.ascend.domain.use_case.interactor.question.QuestionUseCase
import com.doneit.ascend.presentation.login.log_in.LogInContract
import com.doneit.ascend.presentation.login.log_in.LogInFragment
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.utils.Constants.RC_SIGN_IN
import com.doneit.ascend.presentation.utils.LocalStorage
import com.doneit.ascend.presentation.utils.UIReturnStep
import com.facebook.FacebookSdk
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

class LogInActivity : BaseActivity() {
    override fun diModule() = Kodein.Module("LogInActivity") {
        bind<LogInLocalRouter>() with singleton { LogInLocalRouter(this@LogInActivity, instance()) }
        bind<LogInContract.Router>() with singleton { instance<LogInLocalRouter>() }
    }

    private val router: LogInLocalRouter by instance()
    private val localStorage: LocalStorage by instance()
    private val questionUseCase: QuestionUseCase by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(R.layout.activity_log_in)


        val step = localStorage.getUIReturnStep()
        when (step) {
            UIReturnStep.FIRST_TIME_LOGIN -> {
                GlobalScope.launch {
                    val questions = questionUseCase.getQuestionsList()
                    router.navigateToFirstTimeLogin(questions)
                }
            }
            else -> router.navigateToLogInFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            val fragment = supportFragmentManager.findFragmentById(R.id.container)

            if (fragment is LogInFragment) {
                fragment.loginWithGoogle(account?.idToken, account?.displayName)
            }
        } catch (e: ApiException) {
            e.printStackTrace()
        }
    }
}