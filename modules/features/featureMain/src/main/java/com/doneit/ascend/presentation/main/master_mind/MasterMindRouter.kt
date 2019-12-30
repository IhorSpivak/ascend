package com.doneit.ascend.presentation.main.master_mind

import android.content.Intent
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.presentation.main.master_mind.list.ListContract
import com.doneit.ascend.presentation.main.openGroupList
import com.doneit.ascend.presentation.main.openMMInfo
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

    override fun openProfile(model: MasterMindEntity) {
        activity.openMMInfo(model)
    }

    override fun navigateToSearch() {
        activity.startActivity(Intent(activity, SearchActivity::class.java))
    }

    override fun navigateToGroupList(userId: Long) {
        activity.openGroupList(userId)
    }
}