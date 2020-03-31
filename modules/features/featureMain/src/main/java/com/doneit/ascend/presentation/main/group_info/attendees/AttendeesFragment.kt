package com.doneit.ascend.presentation.main.group_info.attendees

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import androidx.lifecycle.Observer
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.group.GroupEntity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.add_member.common.MemberAdapter
import com.doneit.ascend.presentation.main.create_group.add_member.common.MemberListAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentAttendeesBinding
import com.doneit.ascend.presentation.main.group_info.attendees.common.ParticipantsAdapter
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import org.kodein.di.generic.instance

class AttendeesFragment(
    val attendees: MutableList<AttendeeEntity>,
    val group: GroupEntity
): BaseFragment<FragmentAttendeesBinding>() {

    override val viewModelModule = AttendeesViewModelModule.get(this)
    override val viewModel: AttendeesContract.ViewModel by instance()
    private val memberAdapter: MemberListAdapter by lazy {
        MemberListAdapter{
            viewModel.onClearClick(it)
        }
    }

    private val participantsAdapter: ParticipantsAdapter by lazy {
        ParticipantsAdapter()
    }

    private val searchAdapter: MemberAdapter by lazy {
        MemberAdapter(
            {viewModel.onAdd(it) },
            {viewModel.onRemove(it)},
            viewModel
        )
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            lifecycleOwner = this@AttendeesFragment
            model = viewModel
            rvAttendees.adapter = memberAdapter
            rvMembers.adapter = searchAdapter
            rvParticipants.adapter = participantsAdapter
        }
        viewModel.group.postValue(group)
        viewModel.attendees.observe(this, Observer {
            memberAdapter.submitList(it)
        })

        viewModel.users.observe(this, Observer {
            participantsAdapter.submitList(it.toMutableList())
        })
        viewModel.searchResult.observe(this, Observer {
            searchAdapter.submitList(it)
            binding.apply {
                query = tvSearch.text.toString()
            }
            viewModel.apply {
                searchVisibility.postValue(it.isNotEmpty())
                inviteVisibility.postValue(it.isEmpty() && Patterns.EMAIL_ADDRESS
                    .matcher(binding.tvSearch.text.toString()).matches())
            }
        })

        viewModel.validQuery.observe(this, Observer {
            viewModel.inviteButtonActive.postValue(Patterns.EMAIL_ADDRESS.matcher(it).matches())
        })

        viewModel.loadAttendees()

        binding.apply {
            btnBack.setOnClickListener {
                if (viewModel.selectedMembers.size > 0){
                    viewModel.onBackClick(viewModel.selectedMembers.map { it.email?: "" })
                }else{
                    fragmentManager?.popBackStack()
                }
            }
            invite.setOnClickListener {
                viewModel.onInviteClick(listOf(viewModel.validQuery.value!!))
            }
            clearSearch.setOnClickListener {
                tvSearch.text.clear()
                hideKeyboard()
            }
            tvSearch.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    viewModel.onQueryTextChange(p0.toString())
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (p0?.isEmpty()!!){
                        viewModel.apply {
                            searchVisibility.postValue(false)
                            inviteVisibility.postValue(false)
                        }
                    }
                }
            })
        }
    }
}