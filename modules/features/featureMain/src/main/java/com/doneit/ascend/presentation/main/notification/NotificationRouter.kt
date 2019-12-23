package com.doneit.ascend.presentation.main.notification

import android.content.Intent
import com.doneit.ascend.presentation.main.group_info.GroupInfoActivity
import com.vrgsoft.core.presentation.router.FragmentRouter

class NotificationRouter(
    private val activity: NotificationActivity
) : FragmentRouter(activity.supportFragmentManager),
    NotificationContract.Router {

    override val containerId = activity.getContainerId()

    override fun navigateToGroupInfo(id: Long) {
        val intent = Intent(activity, GroupInfoActivity::class.java)
        intent.putExtra(GroupInfoActivity.GROUP_ID, id)
        activity.startActivity(intent)
    }

    override fun closeActivity() {
        activity.finish()
    }
}