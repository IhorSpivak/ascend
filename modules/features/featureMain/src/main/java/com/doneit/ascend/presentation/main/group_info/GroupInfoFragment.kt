package com.doneit.ascend.presentation.main.group_info

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.dialog.DeleteDialog
import com.doneit.ascend.presentation.dialog.QuestionButtonType
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.dialog.SelectPaymentDialog
import com.doneit.ascend.presentation.dialog.common.CardsAdapter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentGroupInfoBinding
import com.doneit.ascend.presentation.utils.CalendarPickerUtil
import com.doneit.ascend.presentation.utils.extensions.toDayMonthYear
import com.doneit.ascend.presentation.utils.showDefaultError
import kotlinx.android.synthetic.main.fragment_group_info.*
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*


class GroupInfoFragment : BaseFragment<FragmentGroupInfoBinding>() {

    override val viewModelModule = GroupInfoViewModelModule.get(this)
    override val viewModel: GroupInfoContract.ViewModel by instance()

    private var currentDialog: AlertDialog? = null
    private val cardsAdapter = CardsAdapter {
        viewModel.onAddPaymentClick()
        currentDialog?.dismiss()
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.model = viewModel

        viewModel.group.observe(this, Observer { group ->
            binding.tvStartDate.text = group.startTime?.toDayMonthYear()

            val builder = StringBuilder()

            if (group.daysOfWeek != null) {//todo refactor
                val list = group.daysOfWeek!!.toMutableList()
                val calendarUtil = CalendarPickerUtil(context!!)//todo move to di

                list.sortBy { it.ordinal }
                for ((index, value) in list.iterator().withIndex()) {
                    if (index != list.size - 1) {
                        builder.append("${calendarUtil.getString(value)}, ")
                    } else {
                        builder.append("${calendarUtil.getString(value)} ")
                    }
                }

                val formatter = SimpleDateFormat("hh:mm aa", Locale.getDefault())
                builder.append(formatter.format(group.startTime))
                binding.tvSchedule.text = builder.toString()
            }
        })

        viewModel.progressDialog.observe(this) {
            it?.let {
                showProgress(it)
            }
        }

        viewModel.cards.observe(viewLifecycleOwner, Observer {
            cardsAdapter.setData(it)
        })


        btnDelete.setOnClickListener {
            currentDialog = DeleteDialog.create(
                context!!,
                getString(R.string.delete_this_group),
                R.string.delete_content,
                R.string.btn_delete,
                R.string.btn_negative
            ) {
                currentDialog?.dismiss()
                when (it) {
                    QuestionButtonType.POSITIVE -> viewModel.deleteGroup()
                }
            }
        }

        ic_abuse.setOnClickListener {
            currentDialog = ReportAbuseDialog.create(context!!) {
                viewModel.report(it)
                currentDialog?.dismiss()
            }
            currentDialog?.show()
        }

        binding.btnSubscribe.setOnClickListener {
            currentDialog = SelectPaymentDialog.create(context!!, cardsAdapter) {
                viewModel.subscribe(it)
            }
            currentDialog?.show()
        }

        binding.btnJoinToDisc.setOnClickListener {
            if(viewModel.isBlocked) {
                showDefaultError(getString(R.string.error_group_user_removed))
            } else {
                viewModel.joinToDiscussion()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val model = arguments!!.getParcelable<GroupEntity>(GROUP_ENTITY)
        if (model != null) {
            viewModel.setModel(model)
        } else {
            val id = arguments!!.getLong(GROUP_ID, -1)
            viewModel.loadData(id)
        }
    }

    companion object {
        const val GROUP_ENTITY = "GROUP_ENTITY"
        const val GROUP_ID = "GROUP_ID"

        fun newInstance(model: GroupEntity): GroupInfoFragment {
            val fragment = GroupInfoFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(GROUP_ENTITY, model)
            }
            return fragment
        }

        fun newInstance(groupId: Long): GroupInfoFragment {
            val fragment = GroupInfoFragment()
            fragment.arguments = Bundle().apply {
                putLong(GROUP_ID, groupId)
            }
            return fragment
        }
    }
}