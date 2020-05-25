package com.doneit.ascend.presentation.video_chat_webinar

import android.content.Intent
import com.doneit.ascend.presentation.utils.extensions.add
import com.doneit.ascend.presentation.utils.extensions.replace
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.owner_options.OwnerOptionsFragment
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.participant_options.ParticipantOptionsFragment
import com.doneit.ascend.presentation.video_chat_webinar.preview.WebinarChatPreviewFragment
import com.doneit.ascend.presentation.video_chat_webinar.questions.QuestionFragment
import com.vrgsoft.core.presentation.router.FragmentRouter

class WebinarVideoChatRouter(
    private val activity: WebinarVideoChatActivity
) : FragmentRouter(activity.supportFragmentManager),
    WebinarVideoChatContract.Router {

    override val containerId = activity.getContainerId()
    private val fullContainerId = activity.getFullContainerId()

    override fun canGoBack(): Boolean {
        return activity.supportFragmentManager.backStackEntryCount != 0
    }

    override fun onBack() {
        activity.supportFragmentManager.popBackStack()
    }

    override fun finishActivity() {
        finishWithResult(WebinarVideoChatActivity.ResultStatus.OK)
    }

    override fun navigateToPreview() {
        activity.supportFragmentManager.replace(containerId, WebinarChatPreviewFragment())
    }

    override fun navigateToChatInProgress() {

    }

    override fun navigateToChatFinishScreen() {
        TODO("Not yet implemented")
    }

    override fun navigateUserChatOptions() {
        activity.supportFragmentManager.add(fullContainerId, ParticipantOptionsFragment())
    }

    override fun navigateToMMChatOptions() {
        activity.supportFragmentManager.add(fullContainerId, OwnerOptionsFragment())
    }

    override fun navigateToChatParticipantActions(userId: String) {
        TODO("Not yet implemented")
    }

    override fun navigateToPermissionsRequiredDialog(resultCode: WebinarVideoChatActivity.ResultStatus) {
        TODO("Not yet implemented")
    }

    override fun navigateToQuestions(groupId: Long) {
        activity.supportFragmentManager.add(fullContainerId, QuestionFragment())
    }

    override fun navigateToChat(groupId: Long) {
        TODO("Not yet implemented")
    }

    override fun navigateToNotes(groupId: Long) {
        TODO("Not yet implemented")
    }

    private fun finishWithResult(resultCode: WebinarVideoChatActivity.ResultStatus) {
        val resultIntent = Intent().apply {
            putExtra(WebinarVideoChatActivity.RESULT_TAG, resultCode)
        }
        activity.setResult(WebinarVideoChatActivity.RESULT_CODE, resultIntent)
        activity.finish()
    }

}