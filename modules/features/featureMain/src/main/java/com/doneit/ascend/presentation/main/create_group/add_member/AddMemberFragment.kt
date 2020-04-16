package com.doneit.ascend.presentation.main.create_group.add_member

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.create_group.CreateGroupHostContract
import com.doneit.ascend.presentation.main.create_group.add_member.common.AddMemberViewModel
import com.doneit.ascend.presentation.main.create_group.add_member.common.MemberAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentAddMemberBinding
import com.doneit.ascend.presentation.models.GroupType
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
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
            {viewModel.onAdd(it) },
            {viewModel.onRemove(it)},
            viewModel
        )
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                viewModel.members.postValue(viewModel.selectedMembers)
                binding.apply {
                    searchVis = false
                    inviteVis = false
                }
                fragmentManager?.popBackStack()
            }
        })
        when(arguments!!.getString(GROUP_TYPE)){
            GroupType.INDIVIDUAL.toString() -> viewModel.canAddMembers.postValue(viewModel.selectedMembers.size < 1)
            GroupType.MASTER_MIND.toString() -> viewModel.canAddMembers.postValue(viewModel.selectedMembers.size < 50)
            GroupType.WEBINAR.toString() -> viewModel.canAddMembers.postValue(viewModel.selectedMembers.size < 3)
            GroupType.SUPPORT.toString() -> viewModel.canAddMembers.postValue(viewModel.selectedMembers.size < 50)
        }
        /*if(arguments!!.getString(GROUP_TYPE) == GroupType.INDIVIDUAL.toString()) {
            viewModel.canAddMembers.postValue(viewModel.selectedMembers.size < 1)
        }else{
            viewModel.canAddMembers.postValue(viewModel.selectedMembers.size < Constants.MAX_MEMBERS_COUNT)
        }
*/
        binding.apply {
            lifecycleOwner = this@AddMemberFragment
            root.apply {
                isFocusableInTouchMode = true
                requestFocus()
                setOnKeyListener { view, i, keyEvent ->
                    if (i == KeyEvent.KEYCODE_BACK){
                        fragmentManager?.popBackStack()
                        true
                    }
                    false
                }
            }
            rvMembers.adapter = memberAdapter
            tvSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener{
                override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                    if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                        hideKeyboard()
                        return true
                    }

                    return false
                }
            })
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
                viewModel.onInviteClick(listOf(tvSearch.text.toString()))
            }
        }

        viewModel.searchResult.observe(this, Observer {
            memberAdapter.submitList(it)
            binding.apply {
                query = tvSearch.text.toString()
                searchVis = it.isNotEmpty()
                inviteVis = it.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(query).matches()
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
        private const val GROUP_TYPE = "type"
        fun getInstance(groupType: GroupType): AddMemberFragment{
            return AddMemberFragment().apply {
                arguments = Bundle().apply {
                    putString(GROUP_TYPE, groupType.toString())
                }
            }
        }
    }
}