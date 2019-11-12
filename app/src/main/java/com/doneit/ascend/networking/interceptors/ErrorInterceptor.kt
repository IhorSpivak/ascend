package com.doneit.ascend.networking.interceptors

import com.doneit.ascend.providers.StorageProvider
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class Error(val errors: List<String>): IllegalAccessException()

class ErrorInterceptor  @Inject constructor(val storage: StorageProvider) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (listOf(401, 402, 403).contains(response.code)) {
            val stringResponse = response.body!!.string()
            throw Gson().fromJson<Error>(stringResponse, Error::class.java)
        }
        return response
    }
}
