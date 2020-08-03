package com.doneit.ascend.retrofit.vimeo

import okhttp3.Interceptor
import okhttp3.Response

internal class VimeoAuthInterceptor(
    //todo: remove hardcoded token and add oAuth2 ??
    private val token: String = "9b98f9db41b7121d506e3238d9cb9629"
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request().newBuilder().let {
            it.addHeader(HEADER_AUTHORIZATION, "Bearer $token")
            it.addHeader(HEADER_CONTENT_TYPE, 	"application/json")
            it.addHeader(HEADER_ACCEPT, "application/vnd.vimeo.*+json;version=3.4")
            chain.proceed(it.build())
        }
    }

    companion object {
        private const val HEADER_AUTHORIZATION = "Authorization"
        private const val HEADER_CONTENT_TYPE = "Content-Type"
        private const val HEADER_ACCEPT = "Accept"

    }
}