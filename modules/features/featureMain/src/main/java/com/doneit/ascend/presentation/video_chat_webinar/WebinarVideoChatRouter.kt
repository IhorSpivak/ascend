package com.doneit.ascend.presentation.video_chat_webinar

import android.content.Intent
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.presentation.main.chats.chat.ChatContract
import com.doneit.ascend.presentation.main.chats.chat.ChatFragment
import com.doneit.ascend.presentation.main.chats.chat.common.ChatType
import com.doneit.ascend.presentation.utils.extensions.add
import com.doneit.ascend.presentation.utils.extensions.replace
import com.doneit.ascend.presentation.utils.extensions.replaceWithBackStack
import com.doneit.ascend.presentation.video_chat.in_progress.user_options.notes.NotesContract
import com.doneit.ascend.presentation.video_chat.in_progress.user_options.notes.NotesFragment
import com.doneit.ascend.presentation.video_chat_webinar.finished.WebinarFinishedFragment
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.WebinarVideoChatInProgressFragment
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.owner_options.OwnerOptionsFragment
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.participant_options.ParticipantOptionsFragment
import com.doneit.ascend.presentation.video_chat_webinar.preview.WebinarChatPreviewFragment
import com.doneit.ascend.presentation.video_chat_webinar.questions.QuestionFragment
import com.vrgsoft.core.presentation.router.FragmentRouter

class WebinarVideoChatRouter(
    private val activity: WebinarVideoChatActivity
) : FragmentRouter(activity.supportFragmentManager),
    NotesContract.Router,
    WebinarVideoChatContract.Router, ChatContract.Router {

    override val containerId = activity.getContainerId()
    private val fullContainerId = activity.getFullContainerId()

    override fun canGoBack(): Boolean {
        return activity.supportFragmentManager.backStackEntryCount != 0
    }

    override fun onBack() {
        activity.userInteractionListener = null
        activity.supportFragmentManager.popBackStack()
    }

    override fun goToDetailedUser(id: Long) {
    }

    override fun goToChatMembers(chatId: Long, members: List<MemberEntity>) {
    }

    override fun finishActivity() {
        finishWithResult(WebinarVideoChatActivity.ResultStatus.OK)
    }

    override fun navigateToPreview() {
        activity.supportFragmentManager.replace(containerId, WebinarChatPreviewFragment())
    }

    override fun navigateToChatInProgress() {
        activity.supportFragmentManager.replace(containerId, WebinarVideoChatInProgressFragment())
    }

    override fun navigateToChatFinishScreen() {
        activity.supportFragmentManager.replace(containerId, WebinarFinishedFragment())
    }

    override fun navigateUserChatOptions() {
        val fragment = ParticipantOptionsFragment()
        activity.userInteractionListener = fragment
        activity.supportFragmentManager.add(fullContainerId, fragment)
    }

    override fun navigateToMMChatOptions() {
        val fragment = OwnerOptionsFragment()
        activity.userInteractionListener = fragment
        activity.supportFragmentManager.add(fullContainerId, fragment)
    }

    override fun navigateToChatParticipantActions(userId: String) {
        TODO("Not yet implemented")
    }

    override fun navigateToPermissionsRequiredDialog(resultCode: WebinarVideoChatActivity.ResultStatus) {
        finishWithResult(resultCode)
    }

    override fun navigateToQuestions(groupId: Long) {
        activity.supportFragmentManager.replaceWithBackStack(fullContainerId, QuestionFragment())
    }

    override fun navigateToChat(chatId: Long) {
        activity.supportFragmentManager.replaceWithBackStack(fullContainerId, ChatFragment.getInstance(chatId, ChatType.WEBINAR_CHAT))
    }

    override fun navigateToNotes(groupId: Long) {
        activity.supportFragmentManager.replaceWithBackStack(fullContainerId, NotesFragment.newInstance(groupId))
    }

    private fun finishWithResult(resultCode: WebinarVideoChatActivity.ResultStatus) {
        val resultIntent = Intent().apply {
            putExtra(WebinarVideoChatActivity.RESULT_TAG, resultCode)
        }
        activity.setResult(WebinarVideoChatActivity.RESULT_CODE, resultIntent)
        activity.finish()
    }

}