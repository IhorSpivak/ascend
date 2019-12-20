package com.doneit.ascend.presentation.main.search

import android.content.Intent
import com.doneit.ascend.presentation.main.group_info.GroupInfoActivity
import com.doneit.ascend.presentation.main.group_list.GroupListActivity
import com.doneit.ascend.presentation.main.master_mind_profile.MMProfileActivity
import com.vrgsoft.core.presentation.router.FragmentRouter

class SearchRouter(
    private val activity: SearchActivity
): FragmentRouter(activity.supportFragmentManager),
    SearchContract.Router {

    override val containerId = activity.getContainerId()

    override fun closeActivity() {
        activity.finish()
    }

    override fun navigateToGroupList(userId: Long) {
        val intent = Intent(activity, GroupListActivity::class.java)

        intent.putExtra(GroupListActivity.ARG_GROUP_TYPE, -1)
        intent.putExtra(GroupListActivity.ARG_USER_ID, userId)

        activity.startActivity(intent)
    }

    //todo replace by activity extensions
    override fun navigateToGroupInfo(id: Long) {
        val intent = Intent(activity, GroupInfoActivity::class.java)
        intent.putExtra(GroupInfoActivity.GROUP_ID, id)
        activity.startActivity(intent)
    }

    override fun navigateToProfile(id: Long) {
        val intent = Intent(activity, MMProfileActivity::class.java)
        intent.putExtra(MMProfileActivity.MM_ID, id)
        activity.startActivity(intent)
    }
}