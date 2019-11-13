package com.vrgsoft.carButler.presentation.splash

import android.accounts.AccountManager
import android.os.Bundle
import com.vrgsoft.core.presentation.activity.BaseActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class SplashActivity : BaseActivity() {
    override fun diModule() = Kodein.Module("SplashActivity") {
    }

    private val router: ISplashRouter by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
    }
}