package com.doneit.ascend.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.common.BottomNavigationChangeListener
import com.doneit.ascend.presentation.main.create_group.CreateGroupActivity
import com.doneit.ascend.presentation.main.extensions.replace
import com.doneit.ascend.presentation.main.extensions.replaceWithBackStack
import com.doneit.ascend.presentation.main.group_list.GroupListContract
import com.doneit.ascend.presentation.main.group_list.GroupListFragment
import com.doneit.ascend.presentation.main.group_list.common.GroupListArgs
import com.doneit.ascend.presentation.main.home.HomeContract
import com.doneit.ascend.presentation.main.home.HomeFragment
import com.doneit.ascend.presentation.profile.ProfileContract
import com.doneit.ascend.presentation.profile.ProfileFragment
import com.vrgsoft.core.presentation.router.FragmentRouter

class MainRouter(
    private val activity: MainActivity,
    private val appRouter: IMainAppRouter
) : FragmentRouter(activity.supportFragmentManager),
    BottomNavigationChangeListener,
    ProfileContract.Router,
    HomeContract.Router,
    GroupListContract.Router {

    override val containerId = activity.getContainerId()

    override fun onBack() {
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

    override fun navigateToGroupList() {
        val args = GroupListArgs(GroupType.MASTER_MIND.ordinal, null)

        val fragment = GroupListFragment()
        (fragment as Fragment).arguments = Bundle().apply {
            putParcelable(ArgumentedFragment.KEY_ARGS, args)
        }

        activity.supportFragmentManager.replaceWithBackStack(containerId, fragment)
    }
}