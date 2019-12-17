package com.doneit.ascend.presentation.main.group_info

import com.vrgsoft.core.presentation.router.FragmentRouter

class GroupInfoRouter (
    private val activity: GroupInfoActivity
): FragmentRouter(activity.supportFragmentManager),
    GroupInfoContract.Router {

    override val containerId = activity.getContainerId()

    override fun closeActivity() {
        activity.finish()
    }
}