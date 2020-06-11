package com.doneit.ascend.presentation.main.home.webinars.common

//Also change class Communities
enum class WebinarFilter {
    RECOVERY,
    FITNESS,
    INDUSTRY,
    FAMILY,
    SPIRITUAL;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}