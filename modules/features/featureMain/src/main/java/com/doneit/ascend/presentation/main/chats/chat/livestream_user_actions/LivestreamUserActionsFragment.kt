package com.doneit.ascend.presentation.main.chats.chat.livestream_user_actions
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.doneit.ascend.domain.entity.chats.MemberEntity
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentLivestreamParticipantActionsBinding
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class LivestreamUserActionsFragment : BaseFragment<FragmentLivestreamParticipantActionsBinding>() {
    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<LivestreamUserActionsContract.ViewModel>() with provider {
            vmShared<WebinarVideoChatViewModel>(
                instance()
            )
        }
    }
    override val viewModel: LivestreamUserActionsContract.ViewModel by instance()

    private var reportAbuseDialog: Dialog? = null

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        val member = requireArguments().getParcelable<MemberEntity>(USER_ID_KEY)!!
        binding.member = member
        binding.tvReport.setOnClickListener {
            showReportAbuseDialog(member.id.toString())
        }

        if(savedInstanceState?.getBoolean(IS_DIALOG_SHOWN_KEY) == true) {
            showReportAbuseDialog(member.id.toString())
        }
    }

    private fun showReportAbuseDialog(participantId: String) {
        reportAbuseDialog = ReportAbuseDialog.create(requireContext(), {
            viewModel.report(it, participantId)
            reportAbuseDialog?.dismiss()
        }, {
            viewModel.block(participantId)
            reportAbuseDialog?.dismiss()
        })
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

        fun newInstance(member: MemberEntity): Fragment {
            val fragment = LivestreamUserActionsFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(USER_ID_KEY, member)
            }
            return fragment
        }
    }
}
