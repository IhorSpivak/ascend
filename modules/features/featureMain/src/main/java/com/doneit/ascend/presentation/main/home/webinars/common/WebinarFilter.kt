package com.doneit.ascend.presentation.main.home.webinars.common

//Also change class Communities
enum class WebinarFilter {
    RECOVERY,
    LIFESTYLE,
    INDUSTRY,
    FAMILY,
    SPIRITUAL,
    SUCCESS;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }
}