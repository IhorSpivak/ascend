package com.doneit.ascend.networking.interceptors

import com.doneit.ascend.providers.StorageProvider
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(val storage: StorageProvider) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (storage.token.isNotEmpty()) {
            val requestBuilder = request.newBuilder()
                .addHeader("Session-Token", storage.token)
            return chain.proceed(requestBuilder.build())
        } else {
            return chain.proceed(request)
        }
    }
}
