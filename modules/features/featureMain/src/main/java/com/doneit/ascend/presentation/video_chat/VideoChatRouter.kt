package com.doneit.ascend.presentation.video_chat

import android.content.Intent
import com.doneit.ascend.presentation.utils.extensions.add
import com.doneit.ascend.presentation.utils.extensions.replace
import com.doneit.ascend.presentation.video_chat.add_mm_plan.AddPlanContract
import com.doneit.ascend.presentation.video_chat.add_mm_plan.AddPlanFragment
import com.doneit.ascend.presentation.video_chat.attachments.AttachmentsArg
import com.doneit.ascend.presentation.video_chat.attachments.AttachmentsContract
import com.doneit.ascend.presentation.video_chat.attachments.AttachmentsFragment
import com.doneit.ascend.presentation.video_chat.attachments.add_url.AddUrlContract
import com.doneit.ascend.presentation.video_chat.attachments.add_url.AddUrlFragment
import com.doneit.ascend.presentation.video_chat.finished.ChatFinishedFragment
import com.doneit.ascend.presentation.video_chat.in_progress.ChatInProgressFragment
import com.doneit.ascend.presentation.video_chat.in_progress.mm_options.MMChatOptionsFragment
import com.doneit.ascend.presentation.video_chat.in_progress.user_actions.ChatParticipantActionsFragment
import com.doneit.ascend.presentation.video_chat.in_progress.user_options.UserChatOptionsFragment
import com.doneit.ascend.presentation.video_chat.in_progress.user_options.notes.NotesContract
import com.doneit.ascend.presentation.video_chat.in_progress.user_options.notes.NotesFragment
import com.doneit.ascend.presentation.video_chat.preview.ChatPreviewFragment
import com.vrgsoft.core.presentation.router.FragmentRouter

class VideoChatRouter(
    private val activity: VideoChatActivity
) : FragmentRouter(activity.supportFragmentManager),
    AttachmentsContract.Router,
    NotesContract.Router,
    AddPlanContract.Router,
    VideoChatContract.Router,
    AddUrlContract.Router {


    override val containerId = activity.getContainerId()
    private val fullContainerId = activity.getFullContainerId()

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
        activity.supportFragmentManager.replace(containerId, ChatFinishedFragment())
    }

    override fun navigateToPermissionsRequiredDialog(resultCode: VideoChatActivity.ResultStatus) {
        finishWithResult(resultCode)
    }

    override fun finishActivity() {
        finishWithResult(VideoChatActivity.ResultStatus.OK)
    }

    override fun navigateUserChatOptions() {
        activity.supportFragmentManager.add(fullContainerId, UserChatOptionsFragment())
    }

    override fun navigateToMMChatOptions() {
        activity.supportFragmentManager.add(fullContainerId, MMChatOptionsFragment())
    }

    override fun navigateToChatParticipantActions(userId: String) {
        activity.supportFragmentManager.add(
            fullContainerId,
            ChatParticipantActionsFragment.newInstance(userId)
        )
    }

    override fun navigateToAttachments(groupId: Long) {
        activity.supportFragmentManager.add(
            fullContainerId, AttachmentsFragment.newInstance(
                AttachmentsArg(groupId)
            )
        )
    }

    override fun navigateToNotes(groupId: Long) {
        activity.supportFragmentManager.add(fullContainerId, NotesFragment.newInstance(groupId))
    }

    private fun finishWithResult(resultCode: VideoChatActivity.ResultStatus) {
        val resultIntent = Intent().apply {
            putExtra(VideoChatActivity.RESULT_TAG, resultCode)
        }
        activity.setResult(VideoChatActivity.RESULT_CODE, resultIntent)
        activity.finish()
    }

    override fun navigateToGoal(groupId: Long) {
        activity.supportFragmentManager.add(fullContainerId, AddPlanFragment.newInstance(groupId))
    }

    override fun toAddLinkFragment(groupId: Long) {
        activity.supportFragmentManager.add(fullContainerId, AddUrlFragment.newInstance(groupId))
    }
}