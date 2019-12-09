package com.doneit.ascend.presentation.main

import android.content.Intent
import com.doneit.ascend.presentation.main.common.BottomNavigationChangeListener
import com.doneit.ascend.presentation.main.create_group.CreateGroupActivity
import com.doneit.ascend.presentation.main.extensions.replace
import com.doneit.ascend.presentation.main.extensions.replaceWithBackStack
import com.doneit.ascend.presentation.main.home.HomeFragment
import com.doneit.ascend.presentation.profile.ProfileContract
import com.doneit.ascend.presentation.profile.ProfileFragment
import com.vrgsoft.core.presentation.router.FragmentRouter

class MainRouter(
    private val activity: MainActivity,
    private val appRouter: IMainAppRouter
) : FragmentRouter(activity.supportFragmentManager),
    BottomNavigationChangeListener,
    ProfileContract.Router {

    override val containerId = activity.getContainerId()

    fun onBack() {
        activity.supportFragmentManager.popBackStack()
    }

    override fun navigateToLogin() {
        appRouter.goToLogin()
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
        activity.supportFragmentManager.replaceWithBackStack(containerId, ProfileFragment())
    }

    fun navigateToCreateGroup() {
        activity.startActivity(Intent(activity, CreateGroupActivity::class.java))
    }
}