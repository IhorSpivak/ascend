package com.doneit.ascend.presentation.main.create_group

import com.doneit.ascend.presentation.main.extensions.replace
import com.vrgsoft.core.presentation.router.FragmentRouter

class CreateGroupRouter(
    private val activity: CreateGroupActivity
) : FragmentRouter(activity.supportFragmentManager) {

    override val containerId = activity.getContainerId()

    fun onBack() {
        activity.finish()
    }

    fun navigateToCreateGroup() {
        activity.supportFragmentManager.replace(containerId, CreateGroupFragment())
    }
}