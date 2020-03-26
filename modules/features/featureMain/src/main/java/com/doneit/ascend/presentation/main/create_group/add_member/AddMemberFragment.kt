package com.doneit.ascend.presentation.main.create_group.add_member

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.create_group.add_member.common.AddMemberViewModel
import com.doneit.ascend.presentation.main.create_group.add_member.common.MemberAdapter
import com.doneit.ascend.presentation.main.create_group.master_mind.CreateMMGroupContract
import com.doneit.ascend.presentation.main.databinding.FragmentAddMemberBinding
import com.doneit.ascend.presentation.utils.extensions.showKeyboard
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class AddMemberFragment : BaseFragment<FragmentAddMemberBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<AddMemberViewModel>() with provider {
            instance<CreateGroupHostContract.ViewModel>()
        }
    }
    override val viewModel: AddMemberViewModel by instance()

    private val memberAdapter: MemberAdapter by lazy {
        MemberAdapter(
            isPublic,
            {viewModel.onAdd(it) },
            {viewModel.onRemove(it)},
            {viewModel.onInviteClick(it)},
            viewModel
        )
    }
    private var isPublic: Boolean = true

    override fun viewCreated(savedInstanceState: Bundle?) {
        isPublic = arguments!!.getBoolean(IS_PUBLIC, true)
        viewModel.canAddMembers.postValue(viewModel.selectedMembers.size < 50)
        binding.apply {
            lifecycleOwner = this@AddMemberFragment
            rvMembers.adapter = memberAdapter
            clearSearch.setOnClickListener {
                tvSearch.text.clear()
                searchVis = false
                inviteVis = false
            }
            btnBack.setOnClickListener {
                viewModel.members.postValue(viewModel.selectedMembers.toMutableList())
                binding.apply {
                    searchVis = false
                    inviteVis = false
                }
                fragmentManager?.popBackStack()
            }

            invite.setOnClickListener {
                viewModel.onInviteClick(tvSearch.text.toString())
            }
        }

        viewModel.searchResult.observe(this, Observer {
            memberAdapter.submitList(it)
            binding.apply {
                query = tvSearch.text.toString()
                searchVis = it.isNotEmpty()
                inviteVis = it.isEmpty()
            }
        })

        binding.tvSearch.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                viewModel.onQueryTextChange(p0.toString())
                binding.inviteActive = Patterns.EMAIL_ADDRESS.matcher(p0.toString()).matches()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0?.isEmpty()!!){
                    binding.apply {
                        searchVis = false
                        inviteVis = false
                    }
                }
            }
        })
        binding.apply{
            tvSearch.showKeyboard()
            searchVis = false
            inviteVis = false
        }
    }

    companion object{
        private const val IS_PUBLIC = "isPublic"
        fun getInstance(individual: Boolean): AddMemberFragment{
            return AddMemberFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(IS_PUBLIC, individual)
                }
            }
        }
    }
}