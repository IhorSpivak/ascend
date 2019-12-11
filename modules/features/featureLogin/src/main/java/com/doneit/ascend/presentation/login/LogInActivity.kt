package com.doneit.ascend.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.login.log_in.LogInContract
import com.doneit.ascend.presentation.main.base.BaseActivity
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.facebook.FacebookSdk
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class LogInActivity : BaseActivity() {
    override fun diModule() = Kodein.Module("LogInActivity") {
        bind<LogInLocalRouter>() with singleton { LogInLocalRouter(this@LogInActivity, instance()) }
        bind<LogInContract.Router>() with singleton { instance<LogInLocalRouter>() }
        bind<LogInActivityContract.Router>() with singleton { instance<LogInLocalRouter>() }

        bind<ViewModelProvider.Factory>() with singleton { CommonViewModelFactory(kodein.direct) }
        bind<ViewModel>(tag = LogInActivityViewModel::class.java.simpleName) with provider {
            LogInActivityViewModel(
                instance(),
                instance(),
                instance(),
                instance()
            )
        }
        bind<LogInActivityContract.ViewModel>() with provider { vm<LogInActivityViewModel>(instance()) }
    }

    private val viewModel: LogInActivityContract.ViewModel by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FacebookSdk.sdkInitialize(applicationContext)
        setContentView(R.layout.activity_log_in)

        viewModel.tryToLogin()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.container)

        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data)
        }
    }

    companion object {
        const val SIGN_UP_VM_TAG = "SignUpFlow"
    }
}