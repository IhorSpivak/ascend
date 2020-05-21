package com.doneit.ascend.presentation.video_chat_webinar.in_progress.participant_options

import android.app.Dialog
import android.os.Bundle
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentWebinarParticipantOptionsBinding
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class ParticipantOptionsFragment : BaseFragment<FragmentWebinarParticipantOptionsBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ParticipantOptionsContract.ViewModel>() with provider {
            vmShared<WebinarVideoChatViewModel>(
                instance()
            )
        }
    }
    override val viewModel: ParticipantOptionsContract.ViewModel by instance()

    private var reportAbuseDialog: Dialog? = null

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        binding.ivReport.setOnClickListener {
            showReportAbuseDialog()
        }

        if (savedInstanceState?.getBoolean(IS_DIALOG_SHOWN_KEY) == true) {
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