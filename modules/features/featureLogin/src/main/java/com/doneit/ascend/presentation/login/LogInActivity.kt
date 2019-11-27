package com.doneit.ascend.presentation.login

import android.os.Bundle
import com.doneit.ascend.domain.use_case.interactor.question.QuestionUseCase
import com.doneit.ascend.presentation.login.log_in.LogInContract
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.utils.LocalStorage
import com.doneit.ascend.presentation.utils.UIReturnStep
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
}