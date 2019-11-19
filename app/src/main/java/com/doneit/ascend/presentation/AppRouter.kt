package com.doneit.ascend.presentation

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import com.doneit.ascend.presentation.login.ILogInRouter
import com.doneit.ascend.presentation.login.LogInActivity
import com.doneit.ascend.presentation.main.IMainRouter
import com.doneit.ascend.presentation.main.MainActivity
import com.doneit.ascend.presentation.splash.ISplashRouter

class AppRouter(private val app: Context) : ISplashRouter, IMainRouter, ILogInRouter {

    override fun goToLogin() {
        startActivity<LogInActivity>(clear = true)
    }

    override fun goToMain() {
        startActivity<MainActivity>(clear = true)
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