package com.doneit.ascend.presentation.main.master_mind_info

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentMasterMindInfoBinding
import kotlinx.android.synthetic.main.fragment_master_mind_info.*
import org.kodein.di.generic.instance

class MMInfoFragment : BaseFragment<FragmentMasterMindInfoBinding>() {

    override val viewModelModule = MMInfoViewModelModule.get(this)
    override val viewModel: MMInfoContract.ViewModel by instance()

    private var currentDialog: AlertDialog? = null

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel

        //todo replace by ArgumentedFragment
        val model = arguments!!.getParcelable<MasterMindEntity>(MM_ENTITY)!!
        viewModel.setModel(model)

        viewModel.sendReportStatus.observe(this) {
            if (it == true) {
                currentDialog?.dismiss()
            } else {
                Toast.makeText(context!!, "Send Error", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.showActionButtons.observe(this) {
            btnFollow.visibility = View.GONE
            btnUnFollow.visibility = View.GONE
        }

        btnInto.setOnClickListener {
            currentDialog = ReportAbuseDialog.create(context!!) {
                viewModel.report(it)
            }
            currentDialog?.show()
        }

        btnBack.setOnClickListener {
            viewModel.goBack()
        }

        rbRatingSet.setOnRatingBarChangeListener { _, rating, fromUser ->
            if (fromUser) {
                viewModel.setRating(rating.toInt())
            }
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
        const val MM_ENTITY = "MM_ENTITY"

        fun newInstance(model: MasterMindEntity): MMInfoFragment {
            val fragment = MMInfoFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(MM_ENTITY, model)
            }
            return fragment
        }
    }
}
