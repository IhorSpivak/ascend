package com.doneit.ascend.presentation.main

import android.content.Intent
import com.doneit.ascend.domain.entity.dto.GroupType
import com.doneit.ascend.presentation.main.common.BottomNavigationChangeListener
import com.doneit.ascend.presentation.main.create_group.CreateGroupActivity
import com.doneit.ascend.presentation.main.extensions.replace
import com.doneit.ascend.presentation.main.extensions.replaceWithBackStack
import com.doneit.ascend.presentation.main.group_info.GroupInfoActivity
import com.doneit.ascend.presentation.main.group_list.GroupListActivity
import com.doneit.ascend.presentation.main.home.HomeContract
import com.doneit.ascend.presentation.main.home.HomeFragment
import com.doneit.ascend.presentation.main.master_mind.MasterMindActivity
import com.doneit.ascend.presentation.main.search.SearchActivity
import com.doneit.ascend.presentation.profile.ProfileContract
import com.doneit.ascend.presentation.profile.ProfileFragment
import com.vrgsoft.core.presentation.router.FragmentRouter

class MainRouter(
    private val activity: MainActivity,
    private val appRouter: IMainAppRouter
) : FragmentRouter(activity.supportFragmentManager),
    BottomNavigationChangeListener,
    ProfileContract.Router,
    HomeContract.Router {

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
}