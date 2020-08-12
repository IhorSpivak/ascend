package com.doneit.ascend.presentation.main.master_mind_info

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.dto.CreateChatDTO
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentMasterMindInfoBinding
import com.doneit.ascend.presentation.utils.Constants
import com.doneit.ascend.presentation.main.home.common.MMProfileTabAdapter
import com.doneit.ascend.presentation.main.home.common.TabAdapter
import kotlinx.android.synthetic.main.fragment_master_mind_info.*
import org.kodein.di.generic.instance

class MMInfoFragment : BaseFragment<FragmentMasterMindInfoBinding>() {

    override val viewModelModule = MMInfoViewModelModule.get(this)
    override val viewModel: MMInfoContract.ViewModel by instance()

    private var currentDialog: AlertDialog? = null
    private var id: Long? = null

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.model = viewModel

        //todo replace by ArgumentedFragment
        id = requireArguments().getLong(USER_ID)
        viewModel.setProfileId(id!!)

        viewModel.sendReportStatus.observe(this) {
            if (it == true) {
                currentDialog?.dismiss()
            } else {
                Toast.makeText(context!!, "Send Error", Toast.LENGTH_LONG).show()
            }
        }

        binding.model = viewModel
        binding.tlGroups.setupWithViewPager(binding.vpGroups)
        binding.vpGroups.offscreenPageLimit = 3


        viewModel.user.observe(this, Observer {
            it ?: return@Observer

            binding.vpGroups.adapter = MMProfileTabAdapter.newInstance(
                childFragmentManager,
                id!!,
                it,
                viewModel.getListOfTitles().map {
                    getString(it)
                }
            )
        })

        btnInto.setOnClickListener {
            currentDialog = ReportAbuseDialog.create(context!!) {
                currentDialog?.dismiss()
                viewModel.report(it)
            }
            currentDialog?.show()
        }

        btnShare.setOnClickListener {
            //TODO: share
            viewModel.onShareClick()
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    Constants.DEEP_LINK_PROFILE_URL + viewModel.profile.value!!.id
                )
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        btnBack.setOnClickListener {
            viewModel.goBack()
        }

        btnMessage.setOnClickListener {

            viewModel.startChatWithMM(mmId = id!!)
        }



        btnInto.setOnClickListener {
            currentDialog = ReportAbuseDialog.create(
                context!!
            ) {
                viewModel.sendReport(it)
            }

            currentDialog?.show()
        }
    }

    companion object {
        const val USER_ID = "MM_ID"

        fun newInstance(id: Long): MMInfoFragment {
            val fragment = MMInfoFragment()
            fragment.arguments = Bundle().apply {
                putLong(USER_ID, id)
            }
            return fragment
        }
    }
}
