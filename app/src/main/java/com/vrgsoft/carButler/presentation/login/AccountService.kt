package com.vrgsoft.carButler.presentation.login

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AccountService : Service() {
    private lateinit var authenticator: Authenticator

    override fun onCreate() {
        super.onCreate()
        authenticator = Authenticator(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return authenticator.iBinder
    }
}