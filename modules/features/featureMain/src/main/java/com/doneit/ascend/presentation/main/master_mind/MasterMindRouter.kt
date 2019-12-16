package com.doneit.ascend.presentation.main.master_mind

import com.vrgsoft.core.presentation.router.FragmentRouter

class MasterMindRouter(
    private val activity: MasterMindActivity
): FragmentRouter(activity.supportFragmentManager),
    MasterMindContract.Router {

    override val containerId = activity.getContainerId()

    override fun closeActivity() {
        activity.finish()
    }
}