package com.doneit.ascend.presentation.main.home.community_feed.channels.create_channel

import com.vrgsoft.core.presentation.router.FragmentRouter

class NewChannelLocalRouter(
    private val parent: CreateChannelFragment,
    private val parentRouter: CreateChannelContract.Router
) : FragmentRouter(parent.childFragmentManager),
    CreateChannelContract.LocalRouter{
    override val containerId = parent.getContainerId()
    override fun onBack() {
        if(parent.childFragmentManager.backStackEntryCount > 0) {
            parent.childFragmentManager.popBackStack()
        }else{
            parentRouter.onBack()
        }
    }

    override fun navigateToAddChannelMembers() {
        //parent.childFragmentManager.add(containerId,  AddMemberFragment())
    }
}