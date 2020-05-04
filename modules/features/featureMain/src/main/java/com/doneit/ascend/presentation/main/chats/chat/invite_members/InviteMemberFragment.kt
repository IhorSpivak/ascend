package com.doneit.ascend.presentation.main.chats.chat.invite_members

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.Observer
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.chats.chat.ChatContract
import com.doneit.ascend.presentation.main.chats.new_chat.add_members.AddMemberContract
import com.doneit.ascend.presentation.main.chats.new_chat.add_members.common.ChatMembersAdapter
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.databinding.FragmentAddMemberBinding
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.doneit.ascend.presentation.utils.extensions.showKeyboard
import com.doneit.ascend.presentation.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_my_chats.*
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class InviteMemberFragment : BaseFragment<FragmentAddMemberBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<AddMemberContract.ViewModel>() with provider {
            instance<ChatContract.ViewModel>()
        }
    }
    override val viewModel: AddMemberContract.ViewModel by instance()

    private val memberAdapter: ChatMembersAdapter by lazy {
        ChatMembersAdapter(
            { viewModel.onAddMember(it) },
            { viewModel.onRemoveMember(it) },
            viewModel
        )
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            viewModel.onQueryTextChange(tvSearch.text.toString())
            lifecycleOwner = this@InviteMemberFragment
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
            tvSearch.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                    if (p1 == EditorInfo.IME_ACTION_SEARCH) {
                        if (tvSearch.text.length > 1) {
                            hideKeyboard()
                        }
                        return true
                    }
                    return false
                }
            })
            clearSearch.setOnClickListener {
                tvSearch.text.clear()
                tvSearch.clearFocus()
                emptyList.gone()
                searchVis = false
            }
            binding.tvSearch.addTextChangedListener(object : TextWatcher {
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
            emptyList.visible(it.isEmpty() && tvSearch.text.length > 1)
            memberAdapter.submitList(it)
            binding.searchVis = it.isNotEmpty()
        })
        binding.tvSearch.showKeyboard()
    }
}