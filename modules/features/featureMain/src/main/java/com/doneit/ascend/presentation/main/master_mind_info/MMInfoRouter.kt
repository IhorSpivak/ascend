package com.doneit.ascend.presentation.main.master_mind_info

import com.doneit.ascend.presentation.openGroupList
import com.vrgsoft.core.presentation.router.FragmentRouter

class MMInfoRouter (
    private val activity: MMInfoActivity
): FragmentRouter(activity.supportFragmentManager),
    MMInfoContract.Router {

    override val containerId = activity.getContainerId()

    override fun closeActivity() {
        activity.finish()
    }

    override fun navigateToGroupList(userId: Long) {
        activity.openGroupList(userId)
    }
}