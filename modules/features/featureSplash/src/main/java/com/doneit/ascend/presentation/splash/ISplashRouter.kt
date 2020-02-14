package com.doneit.ascend.presentation.splash

import android.os.Bundle

interface ISplashRouter {
    fun goToLogin(args: Bundle)
    fun goToMain(args: Bundle)
}