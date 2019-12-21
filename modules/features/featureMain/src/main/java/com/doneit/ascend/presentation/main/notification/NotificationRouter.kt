package com.doneit.ascend.presentation.main.notification

import com.vrgsoft.core.presentation.router.FragmentRouter

class NotificationRouter(
    private val activity: NotificationActivity
) : FragmentRouter(activity.supportFragmentManager),
    NotificationContract.Router {

    override val containerId = activity.getContainerId()

    override fun closeActivity() {
        activity.finish()
    }
}