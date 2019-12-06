package com.doneit.ascend.retrofit.common

object RetrofitConfig {
    lateinit var baseUrl: String
    lateinit var auth: Auth

    internal var enableLogging = false

    fun enableLogging() {
        enableLogging = true
    }
}