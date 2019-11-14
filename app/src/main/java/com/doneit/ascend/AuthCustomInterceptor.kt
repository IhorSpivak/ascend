package com.doneit.ascend

import android.accounts.AccountManager
import com.vrgsoft.retrofit.common.Auth

class AuthCustomInterceptor(
    private val manager: AccountManager,
    private val packageName: String
) : Auth() {
    override fun createAuthHeaderString(): String {
        val accounts = manager.getAccountsByType(packageName)

        if (accounts.isNotEmpty()) {
            val account = accounts[0]
            val token = manager.blockingGetAuthToken(account, "Bearer", false)

            return "Bearer $token"
        }

        return ""
    }
}