package com.doneit.ascend.presentation.main.master_mind

import android.content.Intent
import com.doneit.ascend.presentation.main.group_list.GroupListActivity
import com.doneit.ascend.presentation.main.master_mind.list.ListContract
import com.doneit.ascend.presentation.main.master_mind_profile.MMProfileActivity
import com.doneit.ascend.presentation.main.search.SearchActivity
import com.vrgsoft.core.presentation.router.FragmentRouter

class MasterMindRouter(
    private val activity: MasterMindActivity
): FragmentRouter(activity.supportFragmentManager),
    MasterMindContract.Router,
    ListContract.Router {

    override val containerId = activity.getContainerId()

    override fun closeActivity() {
        activity.finish()
    }

    override fun openProfile(id: Long) {
        //todo replace by MainRouter method invocation
        val intent = Intent(activity, MMProfileActivity::class.java)
        intent.putExtra(MMProfileActivity.MM_ID, id)
        activity.startActivity(intent)
    }

    override fun navigateToSearch() {
        activity.startActivity(Intent(activity, SearchActivity::class.java))
    }

    override fun navigateToGroupList(userId: Long) {
        val intent = Intent(activity, GroupListActivity::class.java)

        intent.putExtra(GroupListActivity.ARG_GROUP_TYPE, -1)
        intent.putExtra(GroupListActivity.ARG_USER_ID, userId)

        activity.startActivity(intent)
    }
}