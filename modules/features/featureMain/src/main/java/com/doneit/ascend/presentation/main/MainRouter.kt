package com.doneit.ascend.presentation.main

import com.doneit.ascend.presentation.main.common.BottomNavigationChangeListener
import com.doneit.ascend.presentation.main.extensions.replace
import com.doneit.ascend.presentation.main.home.HomeFragment
import com.vrgsoft.core.presentation.router.FragmentRouter

class MainRouter(
    private val activity: MainActivity,
    private val appRouter: IMainRouter
) : FragmentRouter(activity.supportFragmentManager),
    BottomNavigationChangeListener {

    override val containerId = activity.getContainerId()

    fun onBack() {
        // TODO: navigate to back
    }

    override fun navigateToHome() {
        activity.supportFragmentManager.replace(containerId, HomeFragment())
    }

    override fun navigateToMyContent() {
        // TODO: navigate to my content screens
    }

    override fun navigateToAscensionPlan() {
        // TODO: navigate to ascension plan screen
    }

    override fun navigateToProfile() {
        // TODO: navigate to profile screen
    }

    fun navigateToCreateGroup() {
        // TODO: navigate to create group screen
    }
}