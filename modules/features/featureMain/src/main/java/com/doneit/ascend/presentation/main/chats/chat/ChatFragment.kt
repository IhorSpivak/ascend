package com.doneit.ascend.presentation.main.chats.chat

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.dialog.EditChatNameDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.chats.chat.common.MessagesAdapter
import com.doneit.ascend.presentation.main.databinding.FragmentChatBinding
import org.kodein.di.generic.instance


class ChatFragment: BaseFragment<FragmentChatBinding>(), PopupMenu.OnMenuItemClickListener{

    override val viewModelModule = ChatViewModelModule.get(this)
    override val viewModel: ChatContract.ViewModel by instance()

    private lateinit var user: UserEntity
    private lateinit var chat: ChatEntity

    private val messagesAdapter: MessagesAdapter by lazy {
        MessagesAdapter(null, null)
    }

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
                if(message.text.toString().isNotBlank()) {
                    viewModel.sendMessage(chat.id, message.text.toString())
                    message.text.clear()
                }
            }
        }
        viewModel.user.observe(this, Observer {
            user = it
            messagesAdapter.updateUser(it)
        })
        viewModel.chatName.observe(viewLifecycleOwner, Observer {
            binding.chatName = it
        })
        viewModel.members.observe(this, Observer {
            if (chat.membersCount > 2){
                binding.apply {
                    chatName = chat.title
                    url = chat.image?.url
                    statusOrCount = resources.getString(R.string.chats_member_count, chat.membersCount)
                }
            }else{
                binding.apply {
                    it.firstOrNull{it.id != user.id}?.let {
                        chatName = it.fullName
                        url = it.image?.url
                        if (it.online) {
                            statusOrCount = resources.getString(R.string.chats_member_online)
                        }
                    }
                }
            }
            messagesAdapter.updateMembers(it)
            viewModel.applyData(chat)
        })
        viewModel.messages.observe(this, Observer {
            messagesAdapter.submitList(it)
            (binding.messageList.layoutManager as LinearLayoutManager).scrollToPosition(it.size - 1)
        })
    }

    override fun onResume() {
        super.onResume()
        arguments?.getParcelable<ChatEntity>(CHAT_KEY)?.let {
            viewModel.loadMembers(it)
            chat = it
        }
    }
    companion object{
        const val CHAT_KEY = "chat"
        fun getInstance(chat: ChatEntity): ChatFragment{
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CHAT_KEY, chat)
                }
            }
        }
    }

    private fun showMenu(v: View){
        when (chat.chatOwnerId) {
            user.id -> {
                if (chat.membersCount > 2){
                    getMenu(v).apply { inflate(R.menu.chat_mm_group_menu) }.show()
                }else{
                    getMenu(v).apply { inflate(R.menu.chat_mm_menu) }.show()
                }
            }
            else -> getMenu(v).apply { inflate(R.menu.chat_ru_group_menu) }.show()
        }
    }
    private fun getMenu(v: View): PopupMenu{
        return PopupMenu(context, v).apply { setOnMenuItemClickListener(this@ChatFragment) }
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        return when (p0!!.itemId) {
            R.id.ru_leave ->{true}
            R.id.ru_report ->{true}
            R.id.mm_delete_chat -> {
                
                true
            }
            R.id.mm_edit_chat ->{
                createEditNameDialog().show()
                true
            }
            R.id.mm_invite_to_chat ->{true}
            R.id.mm_report_user ->{true}
            R.id.mm_delete_group_chat ->{true}
            R.id.mm_block_user ->{true}
            else -> false
        }
    }
    private fun createEditNameDialog(): AlertDialog {
        return EditChatNameDialog.create(
            context!!
        ) {
            viewModel.updateChatName(chat.id, it)
        }
    }
}