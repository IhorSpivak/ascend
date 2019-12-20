package com.doneit.ascend.presentation.main.group_list

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.extensions.replace
import com.doneit.ascend.presentation.main.group_info.GroupInfoActivity
import com.doneit.ascend.presentation.main.group_list.common.GroupListArgs
import com.vrgsoft.core.presentation.router.FragmentRouter

class GroupListRouter(
    private val activity: GroupListActivity
) : FragmentRouter(activity.supportFragmentManager),
    GroupListContract.Router {

    override val containerId = activity.getContainerId()

    override fun onBack() {
        if (activity.supportFragmentManager.backStackEntryCount == 0) {
            activity.finish()
        } else {
            activity.supportFragmentManager.popBackStack()
        }
    }

    fun navigateToStart(groupType: Int, isMyGroups: Int, isAllGroups: Boolean) {
        val args = GroupListArgs(
            groupType,
            when (isMyGroups) {
                0 -> false
                1 -> true
                else -> null
            },
            isAllGroups
        )

        val fragment = GroupListFragment()
        (fragment as Fragment).arguments = Bundle().apply {
            putParcelable(ArgumentedFragment.KEY_ARGS, args)
        }

        activity.supportFragmentManager.replace(containerId, fragment)
    }

    fun navigateToStart(userId: Long) {
        val args = GroupListArgs(null, null, null,  userId)

        val fragment = GroupListFragment()
        (fragment as Fragment).arguments = Bundle().apply {
            putParcelable(ArgumentedFragment.KEY_ARGS, args)
        }

        activity.supportFragmentManager.replace(containerId, fragment)
    }

    override fun navigateToGroupInfo(id: Long) {
        //todo replace by MainRouter method invocation
        val intent = Intent(activity, GroupInfoActivity::class.java)
        intent.putExtra(GroupInfoActivity.GROUP_ID, id)
        activity.startActivity(intent)
    }
}