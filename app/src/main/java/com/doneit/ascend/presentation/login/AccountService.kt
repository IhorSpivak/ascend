package com.doneit.ascend.presentation.login

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AccountService : Service() {
    private lateinit var authenticator: com.doneit.ascend.presentation.login.Authenticator

    override fun onCreate() {
        super.onCreate()
        authenticator = com.doneit.ascend.presentation.login.Authenticator(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return authenticator.iBinder
    }
}