package com.doneit.ascend.presentation.video_chat.in_progress.user_options

import android.app.Dialog
import android.os.Bundle
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentUserChatOptionsBinding
import com.doneit.ascend.presentation.utils.extensions.vmShared
import com.doneit.ascend.presentation.video_chat.VideoChatViewModel
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

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        binding.ivReport.setOnClickListener {
            showReportAbuseDialog()
        }
    }

    private fun showReportAbuseDialog() {
        var dialog: Dialog? = null
        dialog = ReportAbuseDialog.create(context!!) {
            viewModel.reportGroupOwner(it)
            dialog?.dismiss()
        }
        dialog.show()
    }
}