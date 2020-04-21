package com.doneit.ascend.presentation.main.chats.new_chat

import com.doneit.ascend.presentation.main.chats.new_chat.add_members.AddMemberFragment
import com.doneit.ascend.presentation.utils.extensions.add
import com.doneit.ascend.presentation.utils.extensions.replaceWithBackStack
import com.vrgsoft.core.presentation.router.FragmentRouter

class NewChatLocalRouter(
    private val parent: NewChatFragment,
    private val parentRouter: NewChatContract.Router
) : FragmentRouter(parent.childFragmentManager),
    NewChatContract.LocalRouter{
    override val containerId = parent.getContainerId()
    override fun onBack() {
        if(parent.childFragmentManager.backStackEntryCount > 0) {
            parent.childFragmentManager.popBackStack()
        }else{
            parentRouter.onBack()
        }
    }

    override fun navigateToAddChatMember() {
        parent.childFragmentManager.add(containerId,  AddMemberFragment())
    }
}