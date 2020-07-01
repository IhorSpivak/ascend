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
        if (response.code ==  HttpURLConnection.HTTP_UNAUTHORIZED && newRequest.url.toString() != RetrofitConfig.baseUrl + URL_UNAUTHORIZED) {
            Intent(context, LogInActivity::class.java).apply {
                this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                this.putExtra(LOGOUT, LOGOUT)
                context.startActivity(this)
            }
            return response
        }

        return response
    }

    companion object {
        private const val LOGOUT = "logout"
        private const val URL_UNAUTHORIZED = "sessions"
    }
}