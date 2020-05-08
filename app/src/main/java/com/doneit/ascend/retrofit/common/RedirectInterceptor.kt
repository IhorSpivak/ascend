package com.doneit.ascend.retrofit.common

import android.content.Context
import android.content.Intent
import com.doneit.ascend.presentation.login.LogInActivity
import okhttp3.Interceptor
import okhttp3.Response

internal class RedirectInterceptor(
    private val context: Context
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder().build()

        val response = chain.proceed(newRequest)
        if (response.code == 401 && newRequest.url.toString() != RetrofitConfig.baseUrl+"sessions") {
            val intent = Intent(context, LogInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.putExtra(LOGOUT, LOGOUT)
            context.startActivity(intent)
            return response
        }

        return response
    }

    companion object {
        private const val LOGOUT = "logout"
    }
}