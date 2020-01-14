package com.doneit.ascend.presentation.main.video_chat

import com.doneit.ascend.presentation.main.extensions.replace
import com.doneit.ascend.presentation.main.extensions.replaceWithBackStack
import com.doneit.ascend.presentation.main.video_chat.in_progress.ChatInProgressFragment
import com.doneit.ascend.presentation.main.video_chat.preview.ChatPreviewFragment
import com.vrgsoft.core.presentation.router.FragmentRouter

class VideoChatRouter(
    private val activity: VideoChatActivity
) : FragmentRouter(activity.supportFragmentManager),
    VideoChatContract.Router {

    override val containerId = activity.getContainerId()

    override fun onBack() {
        if (activity.supportFragmentManager.backStackEntryCount == 0) {
            activity.finish()
        } else {
            activity.supportFragmentManager.popBackStack()
        }
    }

    /*override fun closeActivity() {
        activity.finish()
    }*/

    override fun navigateToPreview() {
        activity.supportFragmentManager.replace(containerId, ChatPreviewFragment())
    }

    override fun navigateToChatInProgress() {
        activity.supportFragmentManager.replaceWithBackStack(containerId, ChatInProgressFragment())
    }

    override fun navigateToChatFinishScreen() {
        //todo
    }
}