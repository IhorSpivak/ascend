package com.doneit.ascend.presentation.video_chat.in_progress.user_actions

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentChatParticipantActionsBinding
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class ChatParticipantActionsFragment : BaseFragment<FragmentChatParticipantActionsBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ChatParticipantActionsContract.ViewModel>() with provider {
            vmShared<VideoChatViewModel>(
                instance()
            )
        }
    }
    override val viewModel: ChatParticipantActionsContract.ViewModel by instance()

    private var reportAbuseDialog: Dialog? = null

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel
        val userId = arguments!!.getLong(USER_ID_KEY)
        viewModel.participants.observe(viewLifecycleOwner, Observer {
            for(item in it) {
                if(userId == item.userId) {
                    binding.item = item
                    break
                }
            }
        })

        binding.tvReport.setOnClickListener {
            showReportAbuseDialog(userId)
        }

        if(savedInstanceState?.getBoolean(IS_DIALOG_SHOWN_KEY) == true) {
            showReportAbuseDialog(userId)
        }
    }

    private fun showReportAbuseDialog(participantId: Long) {
        reportAbuseDialog = ReportAbuseDialog.create(context!!) {
            viewModel.report(it, participantId)
            reportAbuseDialog?.dismiss()
        }
        reportAbuseDialog?.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        reportAbuseDialog?.let {
            outState.putBoolean(IS_DIALOG_SHOWN_KEY, it.isShowing)
            reportAbuseDialog?.dismiss()
        }
    }

    companion object {
        private const val IS_DIALOG_SHOWN_KEY = "IS_DIALOG_SHOWN_KEY"
        private const val USER_ID_KEY = "USER_ID"

        fun newInstance(userId: Long): Fragment {
            val fragment = ChatParticipantActionsFragment()
            fragment.arguments = Bundle().apply {
                putLong(USER_ID_KEY, userId)
            }
            return fragment
        }
    }
}