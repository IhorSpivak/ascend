package com.doneit.ascend.presentation.main.search

import com.vrgsoft.core.presentation.router.FragmentRouter

class SearchRouter(
    private val activity: SearchActivity
): FragmentRouter(activity.supportFragmentManager),
    SearchContract.Router {

    override val containerId = activity.getContainerId()

    override fun closeActivity() {
        activity.finish()
    }
}