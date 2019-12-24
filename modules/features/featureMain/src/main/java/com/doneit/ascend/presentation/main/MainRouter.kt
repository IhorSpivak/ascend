package com.doneit.ascend.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.presentation.web_page.WebPageFragment
import com.doneit.ascend.presentation.web_page.common.WebPageArgs
import com.doneit.ascend.presentation.main.common.BottomNavigationChangeListener
import com.doneit.ascend.presentation.main.create_group.CreateGroupActivity
import com.doneit.ascend.presentation.main.extensions.replace
import com.doneit.ascend.presentation.main.extensions.replaceWithBackStack
import com.doneit.ascend.presentation.main.group_info.GroupInfoActivity
import com.doneit.ascend.presentation.main.group_list.GroupListActivity
import com.doneit.ascend.presentation.main.home.HomeContract
import com.doneit.ascend.presentation.main.home.HomeFragment
import com.doneit.ascend.presentation.main.master_mind.MasterMindActivity
import com.doneit.ascend.presentation.main.master_mind_profile.MMProfileActivity
import com.doneit.ascend.presentation.main.notification.NotificationActivity
import com.doneit.ascend.presentation.main.search.SearchActivity
import com.doneit.ascend.presentation.profile.ProfileContract
import com.doneit.ascend.presentation.profile.ProfileFragment
import com.doneit.ascend.presentation.web_page.WebPageContract
import com.vrgsoft.core.presentation.fragment.argumented.ArgumentedFragment
import com.vrgsoft.core.presentation.router.FragmentRouter

class MainRouter(
    private val activity: MainActivity,
    private val appRouter: IMainAppRouter
) : FragmentRouter(activity.supportFragmentManager),
    BottomNavigationChangeListener,
    ProfileContract.Router,
    HomeContract.Router,
    WebPageContract.Router {

    override val containerId = activity.getContainerId()

    fun onBack() {
        activity.supportFragmentManager.popBackStack()
    }

    override fun navigateToLogin() {
        appRouter.goToLogin()
    }

    override fun navigateToTerms() {
        val args = WebPageArgs("Terms & Conditions", "terms_and_conditions")

        val fragment = WebPageFragment()
        (fragment as Fragment).arguments = Bundle().apply {
            putParcelable(ArgumentedFragment.KEY_ARGS, args)
        }

        activity.supportFragmentManager.replaceWithBackStack(R.id.container, fragment)
    }

    override fun navigateToPrivacyPolicy() {
        val args = WebPageArgs("Privacy Policy", "privacy_policy")

        val fragment = WebPageFragment()
        (fragment as Fragment).arguments = Bundle().apply {
            putParcelable(ArgumentedFragment.KEY_ARGS, args)
        }

        activity.supportFragmentManager.replaceWithBackStack(R.id.container, fragment)
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

    override fun navigateToGroupList(
        groupType: GroupType?,
        isMyGroups: Boolean?,
        isAllGroups: Boolean
    ) {

        val intent = Intent(activity, GroupListActivity::class.java)
        intent.putExtra(GroupListActivity.ARG_GROUP_TYPE, groupType?.ordinal ?: 0)
        intent.putExtra(
            GroupListActivity.ARG_IS_MINE, when (isMyGroups) {
                null -> -1
                false -> 0
                true -> 1
            }
        )
        intent.putExtra(GroupListActivity.ARG_IS_ALL, isAllGroups)

        activity.startActivity(intent)
    }

    override fun navigateToSearch() {
        activity.startActivity(Intent(activity, SearchActivity::class.java))
    }

    override fun navigateToAllMasterMinds() {
        activity.startActivity(Intent(activity, MasterMindActivity::class.java))
    }

    override fun navigateToGroupInfo(id: Long) {
        val intent = Intent(activity, GroupInfoActivity::class.java)
        intent.putExtra(GroupInfoActivity.GROUP_ID, id)
        activity.startActivity(intent)
    }

    override fun openProfile(id: Long) {
        val intent = Intent(activity, MMProfileActivity::class.java)
        intent.putExtra(MMProfileActivity.MM_ID, id)
        activity.startActivity(intent)
    }

    override fun navigateToNotifications() {
        val intent = Intent(activity, NotificationActivity::class.java)
        activity.startActivity(intent)
    }

    override fun goBack() {
        activity.supportFragmentManager.popBackStack()
    }
}