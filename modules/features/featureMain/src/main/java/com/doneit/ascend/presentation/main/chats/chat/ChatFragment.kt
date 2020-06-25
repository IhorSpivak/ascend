package com.doneit.ascend.presentation.main.chats.chat

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.doneit.ascend.domain.entity.chats.MessageStatus
import com.doneit.ascend.presentation.dialog.BlockUserDialog
import com.doneit.ascend.presentation.dialog.EditChatNameDialog
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.chats.chat.common.ChatType
import com.doneit.ascend.presentation.main.chats.chat.common.MessagesAdapter
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.databinding.FragmentChatBinding
import com.doneit.ascend.presentation.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_my_chats.emptyList
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

    private var currentDialog: AlertDialog? = null
    private var menuResId: Int = -1
    private var kickOrReportUserId: Long = -1
    private val messagesAdapter: MessagesAdapter by lazy {
        MessagesAdapter(
            ChatType.fromRemoteString(requireArguments().getString(CHAT_TYPE).orEmpty()),
            null,
            null,
            { viewModel.onDelete(it) },
            { view, id -> showMenuOnUserClick(view, id) },
            { _, id -> viewModel.showDetailedUser(id) })
    }


    //TODO: still need to refactor
    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            messageList.adapter = messagesAdapter
            menu.setOnClickListener {
                showMenu(it)
            }
            btnBack.setOnClickListener {
                viewModel.onBackPressed()
            }
            send.setOnClickListener {
                if (message.text.toString().isNotBlank()) {
                    viewModel.sendMessage(message.text.toString().trim())
                    message.text.clear()
                }
            }
        }
        viewModel.membersCountGroup.observe(viewLifecycleOwner, Observer {
            binding.statusOrCount =
                resources.getString(R.string.chats_member_count, it)
        })
        viewModel.chat.observe(this, Observer {
            binding.apply {
                binding.menu.visible(it.chatType == ChatType.CHAT)
                binding.chatHeader.visible(it.chatType == ChatType.CHAT)
                binding.tvTitle.visible(it.chatType == ChatType.WEBINAR_CHAT)
                if (it.chatType != ChatType.WEBINAR_CHAT) {
                    chatName = it.chat.title
                    chat = it.chat
                    Glide.with(groupPlaceholder)
                        .load(R.drawable.ic_group_placeholder)
                        .circleCrop()
                        .into(groupPlaceholder)
                    if (it.chat.members?.size != PRIVATE_CHAT_MEMBER_COUNT) {
                        url = it.chat.image?.url
                        statusOrCount =
                            resources.getString(R.string.chats_member_count, it.chat.membersCount)
                    } else {
                        image.setOnClickListener {
                            viewModel.showDetailedUser(
                                chat?.members?.firstOrNull {
                                    it.id != viewModel.user.value?.id
                                }?.id ?: return@setOnClickListener
                            )
                        }
                        it.chat.members?.firstOrNull { it.id != viewModel.user.value?.id }?.let {
                            url = it.image?.url
                            statusOrCount = if (it.online) {
                                resources.getString(R.string.chats_member_online)
                            } else {
                                resources.getString(R.string.chats_member_offline)
                            }
                        }
                    }
                }
            }
            //set type of menu
            when (it.chat.chatOwnerId) {
                it.user.id -> {
                    menuResId = if (it.chat.members?.size != PRIVATE_CHAT_MEMBER_COUNT) {
                        R.menu.chat_mm_group_menu
                    } else {
                        if (it.chat.blocked) {
                            R.menu.chat_mm_menu_unblock
                        } else {
                            R.menu.chat_mm_menu
                        }
                    }
                }
                else -> {
                    menuResId = if (it.chat.members?.size != PRIVATE_CHAT_MEMBER_COUNT) {
                        R.menu.chat_ru_group_menu
                    } else {
                        if (it.chat.blocked) {
                            R.menu.chat_mm_menu_unblock
                        } else {
                            R.menu.chat_mm_menu
                        }
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

            //check when user leaved group chat
            if (it.chat.chatOwnerId != it.user.id) {
                if (it.chat.members?.size != PRIVATE_CHAT_MEMBER_COUNT) {
                    it.chat.members?.firstOrNull { member -> member.id == it.user.id }?.let {
                        if (it.leaved) {
                            binding.apply {
                                message.gone()
                                send.gone()
                            }
                        }
                    }
                }
            }

            messagesAdapter.updateMembers(it.chat)
            messagesAdapter.updateUser(it.user)
        })

        applyDataObserver()
        binding.messageList.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val first =
                        (binding.messageList.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                    val last =
                        (binding.messageList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (last >= 0 && first >= 0) {
                        for (i in last..first) {
                            messagesAdapter.currentList?.let {
                                viewModel.markMessageAsRead(it[i]!!)
                            }
                        }
                    }
                }
            })
        binding.messageList.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (bottom < oldBottom) {
                binding.messageList.postDelayed({
                    binding.messageList.smoothScrollToPosition(
                        0
                    )
                }, 0)
            }
        }
        viewModel.messages.observe(this, Observer
        {
            emptyList.visible(it.isNullOrEmpty())
            messagesAdapter.submitList(it)
        })
    }

    private fun applyDataObserver() {
        messagesAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (itemCount > 0) {
                    val index = messagesAdapter.currentList.orEmpty().indexOfLast {
                        it.status == MessageStatus.SENT && viewModel.user.value!!.id != it.userId
                    }
                    if (index != -1) {
                        scrollToUnread(index)
                    } else {
                        scrollIfNeed()
                    }
                }
            }
        })
    }

    private fun scrollIfNeed() {
        binding.messageList.adapter?.let {
            val lm =
                binding.messageList.layoutManager as LinearLayoutManager
            val first = lm.findFirstVisibleItemPosition()
            if (first < 5) binding.messageList.scrollToPosition(0)
        }
    }

    private fun scrollToUnread(unreadMessageCount: Int = 0) {
        binding.messageList.adapter?.let {
            binding.messageList.scrollToPosition(unreadMessageCount)
            binding.messageList.smoothScrollBy(0, -1)
        }
    }

    override fun onResume() {
        super.onResume()
        arguments?.getString(CHAT_TYPE)?.let {
            viewModel.chatType = ChatType.fromRemoteString(it)
        }
        arguments?.getLong(CHAT_KEY)?.let {
            viewModel.setChat(it)
        }
    }

    companion object {
        const val PRIVATE_CHAT_MEMBER_COUNT = 2 //server sent members count without you.
        const val CHAT_KEY = "chat"
        const val CHAT_TYPE = "chat_type"
        fun getInstance(id: Long, chatType: ChatType = ChatType.CHAT): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putLong(CHAT_KEY, id)
                    putString(CHAT_TYPE, chatType.toString())
                }
            }
        }
    }

    private fun showMenu(v: View) {
        if (menuResId > 0) {
            getMenu(v).apply { inflate(menuResId) }.show()
        }
    }

    private fun showMenuOnUserClick(v: View, id: Long) {
        kickOrReportUserId = id
        getMenu(v).apply { inflate(R.menu.on_user_click_menu) }.show()
    }

    private fun getMenu(v: View): PopupMenu {
        return PopupMenu(context, v).apply { setOnMenuItemClickListener(this@ChatFragment) }
    }

    override fun onMenuItemClick(p0: MenuItem?): Boolean {
        if (hasConnection) {
            return when (p0!!.itemId) {
                R.id.ru_leave -> {
                    context?.let { context ->
                        BlockUserDialog.create(
                            context,
                            getString(R.string.chats_leave),
                            getString(R.string.chats_leave_description),
                            getString(R.string.chats_leave_button),
                            getString(R.string.chats_leave_cancel)
                        ) { viewModel.onLeave() }.show()
                    }
                    true
                }
                R.id.ru_report_group -> {
                    reportOnOwner()
                    true
                }
                R.id.mm_delete_chat -> {
                    context?.let { context ->
                        BlockUserDialog.create(
                            context,
                            getString(R.string.chats_delete),
                            getString(R.string.chats_delete_description),
                            getString(R.string.chats_delete_button),
                            getString(R.string.chats_delete_cancel)
                        ) { viewModel.onDeleteChat() }.show()
                    }
                    true
                }
                R.id.mm_edit_chat -> {
                    currentDialog = createEditNameDialog()
                    currentDialog?.show()
                    true
                }
                R.id.mm_invite_to_chat -> {
                    viewModel.inviteUser()
                    true
                }
                R.id.mm_report_user -> {
                    context?.let { context ->
                        messagesAdapter.user?.let { user ->
                            messagesAdapter.chat?.members?.let { list ->
                                list.firstOrNull { it.id != user.id }?.let { member ->
                                    currentDialog = ReportAbuseDialog.create(
                                        context
                                    ) {
                                        currentDialog?.dismiss()
                                        viewModel.onReport(it, member.id)
                                    }
                                    currentDialog?.show()
                                }
                            }
                        }
                    }
                    true
                }
                R.id.report_single -> {
                    if (kickOrReportUserId > 0) {
                        context?.let { context ->
                            currentDialog = ReportAbuseDialog.create(
                                context
                            ) {
                                currentDialog?.dismiss()
                                viewModel.onReport(it, kickOrReportUserId)
                            }
                            currentDialog?.show()
                        }
                    }
                    true
                }
                R.id.kick -> {

                    true
                }
                R.id.mm_delete_group_chat -> {
                    context?.let { context ->
                        BlockUserDialog.create(
                            context,
                            getString(R.string.chats_delete),
                            getString(R.string.chats_delete_description),
                            getString(R.string.chats_delete_button),
                            getString(R.string.chats_delete_cancel)
                        ) { viewModel.onDeleteChat() }.show()
                    }
                    true
                }
                R.id.mm_block_user -> {
                    context?.let { context ->
                        messagesAdapter.user?.let { user ->
                            messagesAdapter.chat?.members?.let { list ->
                                list.firstOrNull { it.id != user.id }?.let {
                                    BlockUserDialog.create(
                                        context,
                                        getString(R.string.chats_mm_block),
                                        getString(R.string.chats_mm_block_description),
                                        getString(R.string.chats_mm_block_button),
                                        getString(R.string.chats_mm_block_cancel)
                                    ) { viewModel.onBlockUserClick(it) }.show()
                                }
                            }
                        }
                    }
                    true
                }
                R.id.mm_unblock_user -> {
                    context?.let { context ->
                        messagesAdapter.user?.let { user ->
                            messagesAdapter.chat?.members?.let { list ->
                                list.firstOrNull { it.id != user.id }?.let {
                                    BlockUserDialog.create(
                                        context,
                                        getString(R.string.chats_mm_unblock),
                                        getString(R.string.chats_mm_unblock_description),
                                        getString(R.string.chats_mm_unblock_button),
                                        getString(R.string.chats_mm_unblock_cancel)
                                    ) { viewModel.onUnblockUserClick(it) }.show()
                                }
                            }
                        }
                    }
                    true
                }
                else -> false
            }
        } else return false
    }

    private fun reportOnOwner() {
        context?.let { context ->
            currentDialog = ReportAbuseDialog.create(
                context
            ) {
                currentDialog?.dismiss()
                viewModel.onReportChatOwner(it)
            }
            currentDialog?.show()
        }
    }

    override fun onDestroyView() {
        messageList.adapter = null
        super.onDestroyView()
    }

    private fun createEditNameDialog(): AlertDialog {
        return EditChatNameDialog.create(
            requireContext(),
            viewModel.chat.value!!.chat.title
        ) {
            if (hasConnection) {
                viewModel.updateChatName(it)
                currentDialog?.dismiss()
            }
        }
    }

    fun getContainerId() = R.id.new_chat_container
}