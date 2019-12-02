package com.doneit.ascend.presentation.login.utils

interface LoginListener {
    fun loginWithGoogle(idToken: String?, displayName: String?)
}