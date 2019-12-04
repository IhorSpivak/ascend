package com.doneit.ascend.presentation.main.common

interface ToolbarListener {
    fun backButtonChangeVisibility(isShow: Boolean)
    fun searchButtonChangeVisibility(isShow: Boolean)
    fun notificationChangeVisibility(isShow: Boolean)
    fun showHideNotificationBadge(isShow: Boolean)
    fun setCreateGroupState(isEnabled: Boolean)
    fun setTitle(text: String)
}