package com.doneit.ascend

import android.accounts.AccountManager
import com.vrgsoft.retrofit.common.Auth
import java.lang.StringBuilder

class AuthCustomInterceptor(
    private val manager: AccountManager,
    private val packageName: String
) : Auth() {
    override fun createAuthHeaderString(): String {
        val accounts = manager.getAccountsByType(packageName)

        var res = StringBuilder("")
        if (accounts.isNotEmpty()) {
            val account = accounts[0]
            val token = manager.blockingGetAuthToken(account, "Bearer", false)

            res.append("Bearer $token")
        }

        //res.append("?platform=android")

        return res.toString()
    }
}