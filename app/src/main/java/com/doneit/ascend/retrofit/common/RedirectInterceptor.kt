package com.doneit.ascend.retrofit.common

import android.content.Context
import android.content.Intent
import com.doneit.ascend.presentation.login.LogInActivity
import okhttp3.Interceptor
import okhttp3.Response
import java.net.HttpURLConnection

internal class RedirectInterceptor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder().build()

        val response = chain.proceed(newRequest)

        var isUnauthorisedUrl = false
        URL_UNAUTHORIZED.forEach { unauthorisedUrl ->
            if (newRequest.url.toString() == RetrofitConfig.baseUrl + unauthorisedUrl) {
                isUnauthorisedUrl = true
            }
        }

        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED && !isUnauthorisedUrl) {
            Intent(context, LogInActivity::class.java).apply {
                this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this.putExtra(LOGOUT, LOGOUT)
                context.startActivity(this)
            }
            return response
        }

        return response
    }

    companion object {
        private const val LOGOUT = "logout"
        private val URL_UNAUTHORIZED = arrayListOf("sessions", "users/forgot_password")
    }

}