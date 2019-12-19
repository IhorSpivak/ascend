package com.doneit.ascend.presentation.main.master_mind_profile

import com.vrgsoft.core.presentation.router.FragmentRouter

class MMProfileRouter (
    private val activity: MMProfileActivity
): FragmentRouter(activity.supportFragmentManager),
    MMProfileContract.Router {

    override val containerId = activity.getContainerId()

    override fun closeActivity() {
        activity.finish()
    }
}