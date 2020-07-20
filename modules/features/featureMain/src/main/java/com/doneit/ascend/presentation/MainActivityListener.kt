package com.doneit.ascend.presentation

interface MainActivityListener {
    fun setTitle(title: String, isLogoVisible: Boolean = false)
    fun setSearchEnabled(isVisible: Boolean)
    fun setFilterEnabled(isVisible: Boolean)
    fun setChatEnabled(isVisible: Boolean)
    fun getUnreadMessageCount()
}