package com.doneit.ascend.presentation.main.video_chat

import android.content.Intent
import com.doneit.ascend.presentation.main.extensions.replace
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
            finishWithResult(VideoChatActivity.ResultStatus.OK)
        } else {
            activity.supportFragmentManager.popBackStack()
        }
    }

    override fun navigateToPreview() {
        activity.supportFragmentManager.replace(containerId, ChatPreviewFragment())
    }

    override fun navigateToChatInProgress() {
        activity.supportFragmentManager.replace(containerId, ChatInProgressFragment())
    }

    override fun navigateToChatFinishScreen() {
        //todo
    }

    override fun navigateToPermissionsRequiredDialog(resultCode: VideoChatActivity.ResultStatus) {
        finishWithResult(resultCode)
    }

    private fun finishWithResult(resultCode: VideoChatActivity.ResultStatus) {
        val resultIntent = Intent().apply {
            putExtra(VideoChatActivity.RESULT_TAG, resultCode)
        }
        activity.setResult(VideoChatActivity.RESULT_CODE, resultIntent)
        activity.finish()
    }
}