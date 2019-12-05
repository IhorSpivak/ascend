package com.doneit.ascend.presentation.main

import android.content.Intent
import com.doneit.ascend.presentation.main.common.BottomNavigationChangeListener
import com.doneit.ascend.presentation.main.create_group.CreateGroupActivity
import com.doneit.ascend.presentation.main.extensions.replace
import com.doneit.ascend.presentation.main.home.HomeFragment
import com.vrgsoft.core.presentation.router.FragmentRouter

class MainRouter(
    private val activity: MainActivity,
    private val appRouter: IMainRouter
) : FragmentRouter(activity.supportFragmentManager),
    BottomNavigationChangeListener {

    override val containerId = activity.getContainerId()
    private val fullContainerId = activity.getFullContainerId()

    fun onBack() {
        activity.supportFragmentManager.popBackStack()
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
        activity.startActivity(Intent(activity, CreateGroupActivity::class.java))
    }
}