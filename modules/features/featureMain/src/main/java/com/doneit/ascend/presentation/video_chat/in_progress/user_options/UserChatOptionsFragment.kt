package com.doneit.ascend.presentation.video_chat.in_progress.user_options

import android.app.Dialog
import android.os.Bundle
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentUserChatOptionsBinding
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
import kotlinx.android.synthetic.main.dialog_report_abuse.view.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class UserChatOptionsFragment : BaseFragment<FragmentUserChatOptionsBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<UserChatOptionsContract.ViewModel>() with provider {
            vmShared<VideoChatViewModel>(
                instance()
            )
        }
    }
    override val viewModel: UserChatOptionsContract.ViewModel by instance()

    private var reportAbuseDialog: Dialog? = null

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        binding.ivReport.setOnClickListener {
            showReportAbuseDialog()
        }

        if(savedInstanceState?.getBoolean(IS_DIALOG_SHOWN_KEY) == true) {
            showReportAbuseDialog()
        }
    }

    private fun showReportAbuseDialog() {
        reportAbuseDialog = ReportAbuseDialog.create(context!!) {
            viewModel.reportGroupOwner(it)
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
    }
}