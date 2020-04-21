package com.doneit.ascend.presentation.main.chats.new_chat.add_members

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.chats.new_chat.NewChatContract
import com.doneit.ascend.presentation.main.chats.new_chat.add_members.common.ChatMembersAdapter
import com.doneit.ascend.presentation.main.create_group.add_member.common.MemberAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentAddMemberBinding
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.extensions.showKeyboard
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class AddMemberFragment : BaseFragment<FragmentAddMemberBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<AddMemberContract.ViewModel>() with provider {
            instance<NewChatContract.ViewModel>()
        }
    }
    override val viewModel: AddMemberContract.ViewModel by instance()

    private val memberAdapter: ChatMembersAdapter by lazy {
        ChatMembersAdapter(
            {viewModel.onAddMember(it) },
            {viewModel.onRemoveMember(it)},
            viewModel
        )
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            lifecycleOwner = this@AddMemberFragment
            root.apply {
                isFocusableInTouchMode = true
                requestFocus()
                setOnKeyListener { view, i, keyEvent ->
                    if (i == KeyEvent.KEYCODE_BACK) {
                        viewModel.onLocalBackPressed()
                        true
                    }
                    false
                }
            }
            searchVis = false
            inviteVis = false
            rvMembers.adapter = memberAdapter
            tvSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener{
                override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                    if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                        return true
                    }
                    return false
                }
            })
            clearSearch.setOnClickListener {
                tvSearch.text.clear()
                tvSearch.clearFocus()
                hideKeyboard()
                searchVis = false
            }
            binding.tvSearch.addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                    viewModel.onQueryTextChange(p0.toString())
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //empty
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //empty
                }
            })
            btnBack.setOnClickListener {
                viewModel.onLocalBackPressed()
            }
        }
        viewModel.searchResult.observe(this, Observer {
            memberAdapter.submitList(it)
            binding.searchVis = it.isNotEmpty()
        })
        binding.tvSearch.showKeyboard()
    }
}