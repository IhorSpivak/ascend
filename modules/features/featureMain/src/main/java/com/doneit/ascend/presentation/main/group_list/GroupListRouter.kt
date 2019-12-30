package com.doneit.ascend.presentation.main.group_list

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.presentation.main.base.argumented.ArgumentedFragment
import com.doneit.ascend.presentation.main.extensions.replace
import com.doneit.ascend.presentation.main.group_list.common.GroupListArgs
import com.doneit.ascend.presentation.openGroupInfo
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

    fun navigateToStart(args: GroupListArgs) {
        val fragment = GroupListFragment()
        (fragment as Fragment).arguments = Bundle().apply {
            putParcelable(ArgumentedFragment.KEY_ARGS, args)
        }

        activity.supportFragmentManager.replace(containerId, fragment)
    }

    override fun navigateToGroupInfo(model: GroupEntity) {
        activity.openGroupInfo(model)
    }
}