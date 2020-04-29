package com.doneit.ascend.presentation

interface MainActivityListener {
    fun setTitle(title: String)
    fun setSearchEnabled(isVisible: Boolean)
    fun setFilterEnabled(isVisible: Boolean)
    fun setChatEnabled(isVisible: Boolean)
}