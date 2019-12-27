package com.doneit.ascend.presentation.main.search

import android.content.Intent
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.presentation.main.group_info.GroupInfoActivity
import com.doneit.ascend.presentation.main.group_list.GroupListActivity
import com.doneit.ascend.presentation.main.openGroupInfo
import com.doneit.ascend.presentation.main.openMMInfo
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

    override fun navigateToGroupInfo(model: GroupEntity) {
        activity.openGroupInfo(model)
    }

    override fun navigateToMMInfo(model: MasterMindEntity) {
        activity.openMMInfo(model)
    }
}