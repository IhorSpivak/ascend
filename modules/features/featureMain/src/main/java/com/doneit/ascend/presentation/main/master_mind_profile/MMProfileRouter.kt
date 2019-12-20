package com.doneit.ascend.presentation.main.master_mind_profile

import android.content.Intent
import com.doneit.ascend.presentation.main.group_list.GroupListActivity
import com.vrgsoft.core.presentation.router.FragmentRouter

class MMProfileRouter (
    private val activity: MMProfileActivity
): FragmentRouter(activity.supportFragmentManager),
    MMProfileContract.Router {

    override val containerId = activity.getContainerId()

    override fun closeActivity() {
        activity.finish()
    }

    override fun navigateToMMGroups() {

    }

    override fun navigateToGroupList(userId: Long) {

        val intent = Intent(activity, GroupListActivity::class.java)

        intent.putExtra(GroupListActivity.ARG_GROUP_TYPE, -1)
        intent.putExtra(GroupListActivity.ARG_USER_ID, userId)

        activity.startActivity(intent)
    }
}