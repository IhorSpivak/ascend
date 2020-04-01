package com.doneit.ascend.presentation.main.group_info

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.dialog.*
import com.doneit.ascend.presentation.dialog.common.CardsAdapter
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.master_mind.common.InvitedMembersAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentGroupInfoBinding
import com.doneit.ascend.presentation.main.group_info.common.InvitedParticipantAdapter
import com.doneit.ascend.presentation.utils.CalendarPickerUtil
import com.doneit.ascend.presentation.utils.extensions.getTimeFormat
import com.doneit.ascend.presentation.utils.extensions.toDayMonthYear
import com.doneit.ascend.presentation.utils.showDefaultError
import kotlinx.android.synthetic.main.fragment_group_info.*
import org.kodein.di.generic.instance

class GroupInfoFragment : BaseFragment<FragmentGroupInfoBinding>() {

    override val viewModelModule = GroupInfoViewModelModule.get(this)
    override val viewModel: GroupInfoContract.ViewModel by instance()

    private var currentDialog: AlertDialog? = null
    private val cardsAdapter = CardsAdapter {
        viewModel.onAddPaymentClick()
        currentDialog?.dismiss()
    }
    private val membersAdapter: InvitedMembersAdapter by lazy {
        InvitedMembersAdapter{
            viewModel.removeMember(it)
        }
    }
    private val participantAdapter: InvitedParticipantAdapter by lazy {
        InvitedParticipantAdapter()
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            recyclerViewAttendees.adapter = membersAdapter
        }
        viewModel.group.observe(this, Observer { group ->
            binding.apply {
                this.group = group
                tvName.text = group.name
                tvStartDate.text = group.startTime?.toDayMonthYear()
                isAttended = false
            }
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
                builder.append(context!!.getTimeFormat().format(group.startTime))
                binding.tvSchedule.text = builder.toString()
            }
            membersAdapter.submitList(group.attendees)
            binding.viewAttendees.setOnClickListener {
                viewModel.onViewClick(group.attendees?: emptyList())
            }
        })

        viewModel.users.observe(this, Observer {
            if (it.isNotEmpty()){
                binding.recyclerViewAttendees.adapter = participantAdapter
                participantAdapter.participants = it
            }
        })

        viewModel.cards.observe(viewLifecycleOwner, Observer {
            cardsAdapter.setData(it)
        })

        binding.apply {
            mmDelete.setOnClickListener {
                currentDialog = createDeleteDialog()
            }
            indDelete.setOnClickListener {
                currentDialog = createDeleteDialog()
            }
            indCancel.setOnClickListener {
                currentDialog = createCancelDialog()
                currentDialog?.show()
            }

            btnCancelSupport.setOnClickListener {
                currentDialog = createCancelDialog()
                currentDialog?.show()
            }
            supportDelete.setOnClickListener {
                currentDialog = createDeleteDialog()
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

        binding.btnLeaveThisGroup.setOnClickListener {
            currentDialog = createLeaveDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        val id = arguments!!.getLong(GROUP_ID, -1)
        viewModel.loadData(id)
    }

    private fun createCancelDialog(): AlertDialog{
        return CancelDialog.create(
            context!!
        ) {
            viewModel.cancelGroup(it)
            currentDialog?.dismiss()
        }
    }

    private fun createLeaveDialog(): AlertDialog{
        return DeleteDialog.create(
            context!!,
            getString(R.string.leave_this_group_question),
            R.string.leave_content,
            R.string.btn_leave,
            R.string.btn_negative
        ) {
            currentDialog?.dismiss()
            when (it) {
                QuestionButtonType.POSITIVE -> viewModel.leaveGroup()
            }
        }
    }

    private fun createDeleteDialog(): AlertDialog{
        return DeleteDialog.create(
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

    companion object {
        const val GROUP_ID = "GROUP_ID"
        fun newInstance(groupId: Long): GroupInfoFragment {
            val fragment = GroupInfoFragment()
            fragment.arguments = Bundle().apply {
                putLong(GROUP_ID, groupId)
            }
            return fragment
        }
    }
}