package com.doneit.ascend.presentation.main.chats.chat

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.presentation.dialog.BlockUserDialog
import com.doneit.ascend.presentation.dialog.EditChatNameDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.chats.chat.common.MessagesAdapter
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.databinding.FragmentChatBinding
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class ChatFragment : BaseFragment<FragmentChatBinding>(), PopupMenu.OnMenuItemClickListener {

    override val viewModelModule = Kodein.Module(this::class.java.simpleName) {
        bind<ViewModelProvider.Factory>(tag = ChatFragment::class.java.simpleName) with singleton {
            CommonViewModelFactory(
                kodein.direct
            )
        }

        bind<ChatLocalRouter>() with provider {
            ChatLocalRouter(
                this@ChatFragment,
                instance()
            )
        }
        bind<ViewModel>(tag = ChatViewModel::class.java.simpleName) with provider {
            ChatViewModel(
                instance(),
                instance(),
                instance(),
                instance(),
                instance()
            )
        }

        bind<ChatContract.ViewModel>() with provider {
            vm<ChatViewModel>(
                instance(tag = ChatFragment::class.java.simpleName)
            )
        }
    }
    override val viewModel: ChatContract.ViewModel by instance()

    private var menuResId: Int = -1
    private val messagesAdapter: MessagesAdapter by lazy {
        MessagesAdapter(null, null) { viewModel.onDelete(it) }
    }


    //TODO: still need to refactor
    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            messageList.adapter = messagesAdapter
            menu.setOnClickListener {
                showMenu(it)
            }
            btnBack.setOnClickListener {
                viewModel.onBackPressed()
            }
            send.setOnClickListener {
                if (message.text.toString().isNotBlank()) {
                    viewModel.sendMessage(message.text.toString())
                    message.text.clear()
                }
            }
        }
        viewModel.chatName.observe(viewLifecycleOwner, Observer {
            binding.chatName = it
        })
        viewModel.chat.observe(this, Observer {
            binding.apply {
                chatName = it.chat.title
                chat = it.chat
                if (it.chat.membersCount > 2) {
                    url = it.chat.image?.url
                    statusOrCount =
                        resources.getString(R.string.chats_member_count, it.chat.membersCount)

                } else {
                    it.chat.members?.firstOrNull { it.id != viewModel.user.value?.id }?.let {
                        url = it.image?.url
                        if (it.online) {
                            statusOrCount = resources.getString(R.string.chats_member_online)
                        }
                    }

                }
            }
            //set type of menu
            when (it.chat.chatOwnerId) {
                it.user.id -> {
                    if (it.chat.membersCount > 2) {
                        menuResId = R.menu.chat_mm_group_menu
                    } else {
                        menuResId = if (it.chat.blocked) {
                            R.menu.chat_mm_menu_unblock
                        } else {
                            R.menu.chat_mm_menu
                        }
                    }
                }
                else -> {
                    menuResId = if (it.chat.membersCount > 2) {
                        R.menu.chat_ru_group_menu
                    } else {
                        R.menu.chat_ru_menu
                    }
                }
            }
            //if chat is blocked Enter message = gone
            if (it.chat.blocked) {
                binding.apply {
                    message.gone()
                    send.gone()
                }
            }

            messagesAdapter.updateMembers(it.chat.members!!)
            messagesAdapter.updateUser(it.user)
            //viewModel.applyData(chat)
        })
        viewModel.messages.observe(this, Observer {
            messagesAdapter.submitList(it)
            //this work badly, need another solution(trigger on scroll)
            //(binding.messageList.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0,0)
        })
    }

    override fun onResume() {
        super.onResume()
        arguments?.getParcelable<ChatEntity>(CHAT_KEY)?.let {
            viewModel.setChat(it)
        }
    }

    companion object {
        const val CHAT_KEY = "chat"
        fun getInstance(chat: ChatEntity): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CHAT_KEY, chat)
                }
            }
        }
    }

    private fun showMenu(v: View) {
        if (menuResId > 0) {
            getMenu(v).apply { inflate(menuResId) }.show()
        }
    }

    private fun getMenu(v: View): PopupMenu {
        return PopupMenu(context, v).apply { setOnMenuItemClickListener(this@ChatFragment) }
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        return when (p0!!.itemId) {
            R.id.ru_leave -> {
                true
            }
            R.id.ru_report -> {
                true
            }
            R.id.mm_delete_chat -> {
                true
            }
            R.id.mm_edit_chat -> {
                createEditNameDialog().show()
                true
            }
            R.id.mm_invite_to_chat -> {
                viewModel.inviteUser()
                true
            }
            R.id.mm_report_user -> {
                true
            }
            R.id.mm_delete_group_chat -> {
                true
            }
            R.id.mm_block_user -> {
                context?.let { context ->
                    messagesAdapter.user?.let { user ->
                        messagesAdapter.pagedList?.let { list ->
                            list.firstOrNull { it.id != user.id }?.let {
                                BlockUserDialog.create(
                                    context,
                                    getString(R.string.chats_mm_block),
                                    getString(R.string.chats_mm_block_description),
                                    getString(R.string.chats_mm_block_button),
                                    getString(R.string.chats_mm_block_cancel)
                                ) { viewModel.onBlockUserClick(it.id) }.show()
                            }
                        }
                    }
                }
                true
            }
            R.id.mm_unblock_user -> {
                context?.let { context ->
                    messagesAdapter.user?.let { user ->
                        messagesAdapter.pagedList?.let { list ->
                            list.firstOrNull { it.id != user.id }?.let {
                                BlockUserDialog.create(
                                    context,
                                    getString(R.string.chats_mm_unblock),
                                    getString(R.string.chats_mm_unblock_description),
                                    getString(R.string.chats_mm_unblock_button),
                                    getString(R.string.chats_mm_unblock_cancel)
                                ) { viewModel.onUnblockUserClick(it.id) }.show()
                            }
                        }
                    }
                }
                true
            }
            else -> false
        }
    }

    private fun createEditNameDialog(): AlertDialog {
        return EditChatNameDialog.create(
            context!!
        ) {
            viewModel.updateChatName(it)
        }
    }

    fun getContainerId() = R.id.new_chat_container
}