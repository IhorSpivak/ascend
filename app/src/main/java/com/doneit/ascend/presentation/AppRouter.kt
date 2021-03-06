package com.doneit.ascend.presentation

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import com.doneit.ascend.presentation.login.ILogInAppRouter
import com.doneit.ascend.presentation.login.LogInActivity
import com.doneit.ascend.presentation.splash.ISplashRouter

class AppRouter(private val app: Context) : ISplashRouter,
    IMainAppRouter, ILogInAppRouter {

    override fun goToLogin(args: Bundle) {
        startActivity<LogInActivity>(args, clear = true)
    }

    override fun goToMain(args: Bundle) {
        startActivity<MainActivity>(args, clear = true)
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