package com.doneit.ascend.presentation.main.chats.chat

import com.doneit.ascend.presentation.main.chats.chat.invite_members.InviteMemberFragment
import com.doneit.ascend.presentation.utils.extensions.add
import com.vrgsoft.core.presentation.router.FragmentRouter

class ChatLocalRouter(
    private val parent: ChatFragment,
    private val parentRouter: ChatContract.Router
) : FragmentRouter(parent.childFragmentManager),
    ChatContract.LocalRouter {
    override val containerId = parent.getContainerId()
    override fun onBack() {
        if (parent.childFragmentManager.backStackEntryCount > 0) {
            parent.childFragmentManager.popBackStack()
        } else {
            parentRouter.onBack()
        }
    }

    override fun navigateToInviteChatMember() {
        parent.childFragmentManager.add(
            containerId,
            InviteMemberFragment()
        )
    }
}