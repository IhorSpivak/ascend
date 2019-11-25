package com.doneit.ascend.presentation.login

import android.os.Bundle
import com.doneit.ascend.presentation.login.log_in.LogInContract
import com.doneit.ascend.presentation.main.base.BaseActivity
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton


class LogInActivity: BaseActivity() {
    override fun diModule() = Kodein.Module("LogInActivity") {
        bind<LogInLocalRouter>() with singleton { LogInLocalRouter(this@LogInActivity, instance()) }
        bind<LogInContract.Router>() with singleton { instance<LogInLocalRouter>() }
    }

    private val router: LogInLocalRouter by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        router.navigateToLogInFragment()
    }
}