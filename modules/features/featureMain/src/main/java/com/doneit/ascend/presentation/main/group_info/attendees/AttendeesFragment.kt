package com.doneit.ascend.presentation.main.group_info.attendees

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.add_member.common.MemberAdapter
import com.doneit.ascend.presentation.main.create_group.add_member.common.MemberListAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentAddMemberBinding
import com.doneit.ascend.presentation.main.databinding.FragmentAttendeesBinding
import org.kodein.di.generic.instance

class AttendeesFragment(
    val attendees: MutableList<AttendeeEntity>,
    val groupId: Long
): BaseFragment<FragmentAttendeesBinding>() {

    override val viewModelModule = AttendeesViewModelModule.get(this)
    override val viewModel: AttendeesContract.ViewModel by instance()
    private val memberAdapter: MemberListAdapter by lazy {
        MemberListAdapter{
            viewModel.onClearClick(it)
        }
    }

    private val searchAdapter: MemberAdapter by lazy {
        MemberAdapter(
            true,
            {viewModel.onAdd(it) },
            {viewModel.onRemove(it)},
            {viewModel.onInviteClick(it)}
        )
    }
    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this
        binding.rvAttendees.adapter = memberAdapter
        viewModel.groupId.postValue(groupId)
        viewModel.attendees.observe(this, Observer {
            memberAdapter.submitList(it.toMutableList())
        })
        viewModel.attendees.postValue(attendees)
        binding.btnBack.setOnClickListener {
            fragmentManager?.popBackStack()
        }

        binding.tvSearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                //viewModel.submitRequest(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }
}