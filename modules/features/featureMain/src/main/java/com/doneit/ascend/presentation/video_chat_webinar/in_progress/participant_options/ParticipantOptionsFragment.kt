package com.doneit.ascend.presentation.video_chat_webinar.in_progress.participant_options

import android.app.Dialog
import android.os.Bundle
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentWebinarParticipantOptionsBinding
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat_webinar.WebinarVideoChatViewModel
import com.doneit.ascend.presentation.video_chat_webinar.in_progress.common.OnUserInteractionListener
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.util.*

class ParticipantOptionsFragment : BaseFragment<FragmentWebinarParticipantOptionsBinding>(),
    OnUserInteractionListener {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ParticipantOptionsContract.ViewModel>() with provider {
            vmShared<WebinarVideoChatViewModel>(
                instance()
            )
        }
    }
    override val viewModel: ParticipantOptionsContract.ViewModel by instance()
    private var timer = Timer()
    private var reportAbuseDialog: Dialog? = null

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        binding.ivReport.setOnClickListener {
            showReportAbuseDialog()
        }
        binding.btnLeaveThisGroup.setOnClickListener {
            viewModel.leaveGroup()
        }

        if (savedInstanceState?.getBoolean(IS_DIALOG_SHOWN_KEY) == true) {
            showReportAbuseDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            resetTimer()
        } catch (e: IllegalStateException) {
            timer = Timer()
            resetTimer()
        }
    }

    private fun resetTimer() {
        timer.schedule(object : TimerTask() {
            override fun run() {
                viewModel.onBackClick()
                timer.cancel()
                timer.purge()
            }
        }, DELAY_EXIT)
    }


    override fun onUserInteraction() {
        if (isResumed) {
            timer.cancel()
            timer.purge()
            timer = Timer()
            resetTimer()
        }
    }

    private fun showReportAbuseDialog() {
        reportAbuseDialog = ReportAbuseDialog.create(requireContext(), {
            viewModel.reportGroupOwner(it)
            reportAbuseDialog?.dismiss()
        }, {
            viewModel.blockGroupOwner()
            reportAbuseDialog?.dismiss()
        })
        reportAbuseDialog?.show()
    }

    override fun onDestroyView() {
        timer.cancel()
        timer.purge()
        timer = Timer()
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        reportAbuseDialog?.let {
            outState.putBoolean(IS_DIALOG_SHOWN_KEY, it.isShowing)
            reportAbuseDialog?.dismiss()
        }
    }

    companion object {
        private const val DELAY_EXIT = 5000L
        private const val IS_DIALOG_SHOWN_KEY = "IS_DIALOG_SHOWN_KEY"
    }

}