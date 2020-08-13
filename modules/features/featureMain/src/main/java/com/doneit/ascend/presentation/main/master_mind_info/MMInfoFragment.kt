package com.doneit.ascend.presentation.main.master_mind_info

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentMasterMindInfoBinding
import com.doneit.ascend.presentation.main.home.common.MMProfileTabAdapter
import com.doneit.ascend.presentation.utils.Constants
import com.doneit.ascend.presentation.utils.extensions.shareTo
import kotlinx.android.synthetic.main.fragment_master_mind_info.*
import org.kodein.di.generic.instance

class MMInfoFragment : BaseFragment<FragmentMasterMindInfoBinding>() {

    override val viewModelModule = MMInfoViewModelModule.get(this)
    override val viewModel: MMInfoContract.ViewModel by instance()

    private var currentDialog: AlertDialog? = null

    private val userId: Long by lazy {
        requireArguments().getLong(USER_ID)
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        viewModel.setProfileId(userId)
        setupBinding()
        observeData()
        setClickListeners()
    }

    private fun setupBinding() = with(binding) {
        model = viewModel
        tlGroups.setupWithViewPager(vpGroups)
        vpGroups.offscreenPageLimit = 3
    }

    private fun setClickListeners() = with(binding) {
        btnInto.setOnClickListener {
            currentDialog = ReportAbuseDialog.create(requireContext()) {
                currentDialog?.dismiss()
                viewModel.report(it)
            }.also { it.show() }
        }
        btnBack.setOnClickListener {
            viewModel.goBack()
        }
        btnMessage.setOnClickListener {
            viewModel.startChatWithMM(mmId = userId)
        }
        btnInto.setOnClickListener {
            currentDialog = ReportAbuseDialog.create(requireContext()) {
                viewModel.sendReport(it)
            }.also { it.show() }
        }
        btnShare.setOnClickListener {
            shareTo(Constants.DEEP_LINK_PROFILE_URL + viewModel.profile.value!!.id)
        }

        btnBack.setOnClickListener {
            viewModel.goBack()
        }
    }

    private fun observeData() {
        viewModel.sendReportStatus.observe(this) {
            if (it == true) {
                currentDialog?.dismiss()
            } else {
                Toast.makeText(requireContext(), "Send Error", Toast.LENGTH_LONG).show()
            }
        }
        viewModel.user.observe(this, Observer {
            it ?: return@Observer
            binding.vpGroups.adapter = MMProfileTabAdapter.newInstance(
                childFragmentManager,
                userId,
                it,
                viewModel.getListOfTitles().map { titleRes ->
                    getString(titleRes)
                }
            )
        })
    }

    companion object {
        const val USER_ID = "MM_ID"

        fun newInstance(id: Long): MMInfoFragment {
            return MMInfoFragment().apply {
                arguments = Bundle().apply {
                    putLong(USER_ID, id)
                }
            }
        }
    }
}
