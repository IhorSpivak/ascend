package com.vrgsoft.carButler.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.vrgsoft.core.presentation.activity.BaseActivity
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class MainActivity : BaseActivity() {

    override fun diModule() = Kodein.Module("MainActivity") {
        bind<MainRouter>() with provider { MainRouter(this@MainActivity, instance()) }
    }

    private val router: MainRouter by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    companion object {
        fun newInstance(context: Context) = Intent(context, MainActivity::class.java)
    }
}