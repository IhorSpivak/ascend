package com.doneit.ascend.presentation.main.search

import com.doneit.ascend.domain.entity.GroupEntity
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.presentation.main.openGroupInfo
import com.doneit.ascend.presentation.main.openGroupList
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
        activity.openGroupList(userId)
    }

    override fun navigateToGroupInfo(model: GroupEntity) {
        activity.openGroupInfo(model)
    }

    override fun navigateToMMInfo(model: MasterMindEntity) {
        activity.openMMInfo(model)
    }
}