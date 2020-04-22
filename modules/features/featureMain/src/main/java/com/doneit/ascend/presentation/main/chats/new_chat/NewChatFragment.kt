package com.doneit.ascend.presentation.main.chats.new_chat

import android.os.Bundle
import android.view.KeyEvent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.chats.new_chat.common.ChatMemberAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentNewChatBinding
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class NewChatFragment: BaseFragment<FragmentNewChatBinding>() {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ViewModelProvider.Factory>(tag=NewChatFragment::class.java.simpleName) with singleton { CommonViewModelFactory(kodein.direct) }

        bind<NewChatLocalRouter>() with provider {
            NewChatLocalRouter(
                this@NewChatFragment,
                instance()
            )
        }
        bind<ViewModel>(tag = NewChatViewModel::class.java.simpleName) with provider {
            NewChatViewModel(
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind<NewChatContract.ViewModel>() with provider {
            vm<NewChatViewModel>(
                instance(tag=NewChatFragment::class.java.simpleName)
            )
        }
    }
    override val viewModel: NewChatContract.ViewModel by instance()

    private val memberAdapter: ChatMemberAdapter by lazy {
        ChatMemberAdapter{viewModel.onDeleteMember(it.id)}
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
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
            model = viewModel
            recyclerViewAddedMembers.adapter = memberAdapter
            buttonComplete.setOnClickListener {
                viewModel.complete()
            }
        }
        viewModel.addedMembers.observe(this, Observer {
            memberAdapter.submitList(it)
        })
    }

    fun getContainerId() = R.id.new_chat_container
}