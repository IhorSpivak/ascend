package com.doneit.ascend.presentation.video_chat_webinar

import com.doneit.ascend.presentation.utils.extensions.add
import com.doneit.ascend.presentation.utils.extensions.replace
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.participant_options.ParticipantOptionsFragment
import com.doneit.ascend.presentation.video_chat_webinar.preview.WebinarChatPreviewFragment
import com.vrgsoft.core.presentation.router.FragmentRouter

class WebinarVideoChatRouter(
    private val activity: WebinarVideoChatActivity
) : FragmentRouter(activity.supportFragmentManager),
    WebinarVideoChatContract.Router {

    override val containerId = activity.getContainerId()
    private val fullContainerId = activity.getFullContainerId()
    override fun canGoBack(): Boolean {
        TODO("Not yet implemented")
    }

    override fun onBack() {
        TODO("Not yet implemented")
    }

    override fun finishActivity() {
        TODO("Not yet implemented")
    }

    override fun navigateToPreview() {
        activity.supportFragmentManager.replace(containerId, WebinarChatPreviewFragment())
    }

    override fun navigateToChatInProgress() {
        TODO("Not yet implemented")
    }

    override fun navigateToChatFinishScreen() {
        TODO("Not yet implemented")
    }

    override fun navigateUserChatOptions() {
        activity.supportFragmentManager.add(fullContainerId, ParticipantOptionsFragment())
    }

    override fun navigateToMMChatOptions() {
        TODO("Not yet implemented")
    }

    override fun navigateToChatParticipantActions(userId: String) {
        TODO("Not yet implemented")
    }

    override fun navigateToPermissionsRequiredDialog(resultCode: WebinarVideoChatActivity.ResultStatus) {
        TODO("Not yet implemented")
    }

    override fun navigateToQuestions(groupId: Long) {
        TODO("Not yet implemented")
    }

    override fun navigateToChat(groupId: Long) {
        TODO("Not yet implemented")
    }

    override fun navigateToNotes(groupId: Long) {
        TODO("Not yet implemented")
    }

}