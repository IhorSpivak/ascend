package com.vrgsoft.carButler.presentation

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import com.vrgsoft.carButler.presentation.main.IMainRouter
import com.vrgsoft.carButler.presentation.splash.ISplashRouter

class AppRouter(private val app: Context) : ISplashRouter, IMainRouter {

    override fun goToLogin() {
        //startActivity<LoginActivity>(clear = true)//todo: if it need
    }

    override fun goToMain() {
//        navigateToMain()
    }

    private inline fun <reified T : Any> startActivity(args: Bundle = Bundle(), clear: Boolean = false) {
        val intent = Intent(app, T::class.java).apply {
            putExtras(args)

            if (clear) {
                addFlags(FLAG_ACTIVITY_CLEAR_TASK)
            }

            addFlags(FLAG_ACTIVITY_NEW_TASK)
        }

        app.startActivity(intent)
    }
}