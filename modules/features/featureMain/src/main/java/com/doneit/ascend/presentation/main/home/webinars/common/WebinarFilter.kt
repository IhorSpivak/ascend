package com.doneit.ascend.presentation.main.home.webinars.common

enum class WebinarFilter {
    RECOVERY,
    SUCCESS,
    FAMILY,
    SPIRITUAL;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}