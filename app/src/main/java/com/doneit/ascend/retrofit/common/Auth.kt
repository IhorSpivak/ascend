package com.doneit.ascend.retrofit.common

abstract class Auth {
    abstract fun createAuthHeaderString(): String
}