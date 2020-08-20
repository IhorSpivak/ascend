package com.doneit.ascend.retrofit.common

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

class ContentTypeInterceptor(
    private val loggingInterceptor: HttpLoggingInterceptor
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()
        val request: Request = original.newBuilder().build()

        val hasMultipart: Boolean = request.body
            ?.contentType()
            .toString()
            .contains("multipart/form-data")

        loggingInterceptor.level = if (hasMultipart) HttpLoggingInterceptor.Level.NONE
        else HttpLoggingInterceptor.Level.BODY
        return chain.proceed(request)
    }
}