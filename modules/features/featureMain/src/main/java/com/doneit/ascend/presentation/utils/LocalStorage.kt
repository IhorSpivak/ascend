package com.doneit.ascend.presentation.utils

import android.content.Context

class LocalStorage(private val context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)

    fun saveSessionToken(token: String) {
        with(sharedPreferences.edit()) {
            putString(ARG_SESSION_TOKEN, token)
            apply()
        }
    }

    fun getSessionToken(): String {
        return sharedPreferences.getString(ARG_SESSION_TOKEN, "") ?: ""
    }

    companion object {
        private const val APP_PREFERENCES = "app_settings"

        private const val ARG_SESSION_TOKEN = "app_session_token"
    }
}