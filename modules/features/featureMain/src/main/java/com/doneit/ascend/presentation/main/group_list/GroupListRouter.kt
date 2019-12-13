package com.doneit.ascend.presentation.main.group_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.extensions.replace
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

    fun navigateToStart(groupType: Int) {
        val args = GroupListArgs(groupType, null)

        val fragment = GroupListFragment()
        (fragment as Fragment).arguments = Bundle().apply {
            putParcelable(ArgumentedFragment.KEY_ARGS, args)
        }

        activity.supportFragmentManager.replace(containerId, fragment)
    }
}