package com.doneit.ascend.presentation.main.group_info.attendees

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.add_member.common.MemberListAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentAddMemberBinding
import org.kodein.di.generic.instance

class AttendeesFragment : BaseFragment<FragmentAddMemberBinding>() {

    override val viewModelModule = AttendeesViewModelModule.get(this)
    override val viewModel: AttendeesContract.ViewModel by instance()
    private val memberAdapter: MemberListAdapter by lazy {
        MemberListAdapter()
    }
    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.lifecycleOwner = this

        binding.rvMembers.adapter = memberAdapter

        memberAdapter.members = listOf("NAME1", "NAME2","NAME3")

        binding.btnBack.setOnClickListener {
            viewModel.goBack()
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