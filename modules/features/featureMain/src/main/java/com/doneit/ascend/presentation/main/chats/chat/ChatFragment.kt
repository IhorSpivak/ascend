package com.doneit.ascend.presentation.main.chats.chat

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.core.view.doOnNextLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.androidisland.ezpermission.EzPermission
import com.bumptech.glide.Glide
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.chats.MessageEntity
import com.doneit.ascend.domain.entity.chats.MessageStatus
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.dialog.BlockUserDialog
import com.doneit.ascend.presentation.dialog.ChooseImageBottomDialog
import com.doneit.ascend.presentation.dialog.ChooseImageBottomDialog.AllowedIntents
import com.doneit.ascend.presentation.dialog.EditChatNameDialog
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.base.CommonViewModelFactory
import com.doneit.ascend.presentation.main.chats.chat.common.ChatType
import com.doneit.ascend.presentation.main.chats.chat.common.MessagesAdapter
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.databinding.FragmentChatBinding
import com.doneit.ascend.presentation.models.chat.ChatWithUser
import com.doneit.ascend.presentation.utils.MediaValidator
import com.doneit.ascend.presentation.utils.extensions.doOnGlobalLayout
import com.doneit.ascend.presentation.utils.extensions.requestPermissions
import com.doneit.ascend.presentation.utils.extensions.visible
import com.doneit.ascend.presentation.utils.showDefaultError
import kotlinx.android.synthetic.main.fragment_chat.*
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.io.File
import java.util.*
import com.doneit.ascend.domain.entity.dto.ChatType as GeneralChatType
import com.doneit.ascend.domain.entity.dto.ChatType as InnerChatType


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
                instance(),
                chatWithUser
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
    private var messageListRenderCount = 0
    private var hasEnded = true
    private var lastFileUri = Uri.EMPTY

    private val chatWithUser: ChatWithUser by lazy {
        ChatWithUser(
            requireArguments().getParcelable(CHAT_KEY)!!,
            requireArguments().getParcelable(CHAT_USER)!!,
            ChatType.fromRemoteString(requireArguments().getString(CHAT_TYPE)!!)
        )
    }
    private val messagesAdapter: MessagesAdapter by lazy {
        MessagesAdapter(
            chatWithUser,
            viewModel::onDelete,
            ::showMenuOnUserClick,
            { _, id -> viewModel.showDetailedUser(id) },
            { _, member -> viewModel.showLiveStreamUser(member) },
            {
                it.contentType?.let { _ ->
                    viewModel.previewAttachment(it)
                } ?: kotlin.run {
                    val downloadsFolder =
                        requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                    previewFile(File(downloadsFolder, it.name).toUri())
                }
            },
            viewModel::showPostDetails,
            { viewModel.showDetailedUser(it.id) },
            viewModel::showGroup,
            viewModel::cancelUpload
        )
    }

    private fun previewFile(uri: Uri) {
        Intent(Intent.ACTION_VIEW).apply {
            val fileUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName + ".fileprovider",
                File(uri.path.orEmpty())
            )
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
            data = fileUri
            try {
                startActivity(Intent.createChooser(this, getString(R.string.preview)))
            } catch (e: ActivityNotFoundException) {
            }
        }
    }

    fun getContainerId() = R.id.new_chat_container

    override fun viewCreated(savedInstanceState: Bundle?) {
        binding.apply {
            model = viewModel
            messageList.adapter = messagesAdapter
            messageList.itemAnimator = null
            menu.visible(chatWithUser.chatType == ChatType.CHAT)
            chatHeader.visible(chatWithUser.chatType == ChatType.CHAT)
            tvTitle.visible(chatWithUser.chatType == ChatType.WEBINAR_CHAT)
            closeInputIfBlocked()
            applyDefaultChatLayout()
            setClickListeners()
        }
        //set type of menu
        defineMenuResId()
        observeData()
        scrollLogic()
        attachGlobalLayoutListener()
    }

    private fun attachGlobalLayoutListener() {
        binding.messageList.doOnGlobalLayout {
            if (isResumed) {
                performScrollWithStatus(messagesAdapter.currentList.orEmpty())
            }
        }
    }

    private fun scrollLogic() {
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

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    hasEnded = newState == SCROLL_STATE_IDLE
                }
            })
    }

    private fun FragmentChatBinding.closeInputIfBlocked() {
        fun hide() {
            message.gone()
            send.gone()
            addAttachments.gone()
        }
        //if chat is blocked Enter message = gone
        if (chatWithUser.chat.blocked || (chatWithUser.chat.isPrivate && !chatWithUser.isOwner())) {
            hide()
        }
        //check when user leaved group chat
        if (chatWithUser.chat.chatOwnerId != chatWithUser.user.id) {
            if (!chatWithUser.chat.isPrivate) {
                chatWithUser.chat.members
                    .firstOrNull { it.id == chatWithUser.user.id }
                    ?.let {
                        if (it.leaved) {
                            hide()
                        }
                    }
            }
        }
    }

    private fun FragmentChatBinding.applyDefaultChatLayout() {
        if (chatWithUser.chatType != ChatType.WEBINAR_CHAT) {
            chat = chatWithUser.chat
            when (chatWithUser.chat.chatType) {
                InnerChatType.CHAT -> {
                    if (chatWithUser.chat.members.size != PRIVATE_CHAT_MEMBER_COUNT) {
                        url = chatWithUser.chat.image?.url
                        statusOrCount = resources.getString(
                            R.string.chats_member_count,
                            chatWithUser.chat.membersCount
                        )
                        Glide.with(groupPlaceholder)
                            .load(R.drawable.ic_group_placeholder)
                            .circleCrop()
                            .into(groupPlaceholder)
                    } else {
                        image.setOnClickListener {
                            viewModel.showDetailedUser(
                                chatWithUser.chat.members.firstOrNull {
                                    it.id != chatWithUser.user.id
                                }?.id ?: return@setOnClickListener
                            )
                        }
                        chatWithUser.chat.members.firstOrNull { it.id != chatWithUser.user.id }
                            ?.let {
                                url = it.image?.url
                                statusOrCount = resources.getString(
                                    if (it.online) {
                                        R.string.chats_member_online
                                    } else {
                                        R.string.chats_member_offline
                                    }
                                )
                            }
                    }
                }
                InnerChatType.CHANNEL -> {
                    Glide.with(groupPlaceholder)
                        .load(chatWithUser.chat.image?.url)
                        .circleCrop()
                        .into(groupPlaceholder)
                    statusOrCount = resources.getString(
                        R.string.chats_member_count,
                        chatWithUser.chat.membersCount
                    )
                }
            }
        }
    }

    private fun FragmentChatBinding.setClickListeners() {
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
        addAttachments.setOnClickListener {
            doIfPermissionsGranted {
                ChooseImageBottomDialog.create(
                    arrayOf(
                        AllowedIntents.IMAGE,
                        AllowedIntents.CAMERA,
                        AllowedIntents.VIDEO,
                        AllowedIntents.FILE
                    ),
                    {
                        startActivityForResult(
                            getCameraIntent(),
                            REQUEST_CODE_CAMERA
                        )
                    },
                    {
                        startActivityForResult(
                            getMediaIntent(),
                            REQUEST_CODE_GALLERY
                        )
                    },
                    {
                        startActivityForResult(
                            getVideoIntent(),
                            REQUEST_CODE_VIDEO
                        )
                    },
                    {
                        startActivityForResult(
                            getFileIntent(),
                            REQUEST_CODE_FILE
                        )
                    }
                ).show(childFragmentManager, ChooseImageBottomDialog::class.java.simpleName)
            }
        }
    }

    private fun getCameraIntent(): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            lastFileUri = FileProvider.getUriForFile(
                requireContext(),
                requireContext().applicationContext.packageName + ".fileprovider",
                createImageFile()
            )
            putExtra(
                MediaStore.EXTRA_OUTPUT, lastFileUri
            )
        }
    }

    private fun getMediaIntent(): Intent {
        return Intent.createChooser(
            Intent(Intent.ACTION_GET_CONTENT).apply {
                putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                type = MIME_TYPE_IMAGE
            }, getString(R.string.select_from_gallery)
        )
    }

    private fun getVideoIntent(): Intent {
        return Intent.createChooser(
            Intent(Intent.ACTION_GET_CONTENT).apply {
                putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                type = MIME_TYPE_VIDEO
            }, getString(R.string.take_video)
        )
    }

    private fun getFileIntent(): Intent {
        return Intent.createChooser(
            Intent(Intent.ACTION_GET_CONTENT).apply {
                putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                type = MIME_TYPE_ALL
            }, getString(R.string.take_file)
        )
    }

    private fun createImageFile(): File {
        return File(
            requireContext()
                .getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES
                ), "${IMAGE_FILENAME}${UUID.randomUUID()}.jpg"
        )
    }

    private fun doIfPermissionsGranted(action: () -> Unit) {
        val isGrantedStorage = EzPermission.isGranted(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val isGrantedCamera = EzPermission.isGranted(
            requireContext(),
            Manifest.permission.CAMERA
        )
        if (!isGrantedCamera || !isGrantedStorage) {
            requireContext().requestPermissions(
                listOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ),
                onGranted = {
                    action()
                },
                onDenied = {
                }
            )
        } else action()
    }

    private fun defineMenuResId() {
        menuResId = if (chatWithUser.chat.members.size != PRIVATE_CHAT_MEMBER_COUNT) {
            if (chatWithUser.user.id == chatWithUser.chat.chatOwnerId) {
                if (chatWithUser.chat.chatType == GeneralChatType.CHAT) {
                    R.menu.chat_mm_group_menu
                } else {
                    R.menu.mm_channel_menu
                }
            } else {
                if (chatWithUser.chat.chatType == GeneralChatType.CHAT) {
                    R.menu.chat_ru_group_menu
                } else {
                    R.menu.channel_ru_menu
                }
            }
        } else {
            if (chatWithUser.chat.chatType == GeneralChatType.CHAT) {
                if (chatWithUser.chat.blocked) {
                    R.menu.chat_mm_menu_unblock
                } else {
                    R.menu.chat_mm_menu
                }
            } else {
                if (chatWithUser.user.id == chatWithUser.chat.chatOwnerId) {
                    R.menu.mm_channel_menu
                } else {
                    R.menu.channel_ru_menu
                }
            }
        }
    }

    private fun observeData() {
        with(viewModel) {
            observe(membersCountGroup, ::handleGroupCount)
            observe(messages, ::handleMessages)
            observe(chat, ::handleChat)
        }
    }

    private fun handleChat(chat: ChatWithUser) {
        binding.chat = chat.chat
        if (chatWithUser.chat.members.size != PRIVATE_CHAT_MEMBER_COUNT || chat.chat.chatType == InnerChatType.CHANNEL) {
            binding.url = chatWithUser.chat.image?.url
            binding.statusOrCount = resources.getString(
                R.string.chats_member_count,
                chat.chat.membersCount
            )
        } else {
            chatWithUser.chat.members.firstOrNull { it.id != chatWithUser.user.id }?.let {
                binding.url = it.image?.url
                binding.statusOrCount = resources.getString(
                    if (it.online) {
                        R.string.chats_member_online
                    } else {
                        R.string.chats_member_offline
                    }
                )
            }
        }
    }

    private fun handleMessages(list: PagedList<MessageEntity>) = with(binding) {
        messagesAdapter.submitList(list)
        messageList.doOnNextLayout {
            if (messageListRenderCount <= 1) {
                (messageList.layoutManager as LinearLayoutManager).scrollToPosition(0)
            }
            messageListRenderCount++
        }
        performScrollWithStatus(list)
    }

    private fun performScrollWithStatus(list: List<MessageEntity>) {
        if (list.isNotEmpty()) {
            val index = list.indexOfLast {
                it.status == MessageStatus.SENT && chatWithUser.user.id != it.userId
            }
            if (index != -1) {
                scrollToUnread(index)
            } else {
                scrollIfNeed()
            }
        }
    }

    private fun scrollIfNeed() {
        binding.messageList.adapter?.let {
            val lm = binding.messageList.layoutManager as LinearLayoutManager
            val first = lm.findFirstCompletelyVisibleItemPosition()
            if (first < 2 && first != 0 && hasEnded) {
                scrollToPos(0)
            }
        }
    }

    private fun scrollToUnread(unreadMessageCount: Int = 0) {
        scrollToPos(unreadMessageCount)
        binding.messageList.smoothScrollBy(0, -1)
    }

    private fun scrollToPos(pos: Int) {
        if (isResumed) {
            with(binding.messageList.layoutManager as LinearLayoutManager) {
                scrollToPosition(pos)
            }
        }
    }

    private fun handleGroupCount(count: Int) {
        binding.statusOrCount = resources.getString(R.string.chats_member_count, count)
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

    override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
        menuItem ?: return false
        if (hasConnection) {
            return when (menuItem.itemId) {

                R.id.mm_user_list -> {
                    viewModel.goToUserList(chatWithUser.chat)
                    true
                }
                R.id.mm_add_members -> {
                    viewModel.inviteUser()
                    true
                }

                R.id.mm_edit_channel -> {
                    viewModel.goToEditChannel(chatWithUser.chat)
                    true
                }

                R.id.mm_delete_channel -> {
                    showDeleteChannelDialog()
                    true
                }

                R.id.leave_channel -> {
                    showLeaveChannelDialog()
                    true
                }
                R.id.report_channel -> {
                    reportOnOwner()
                    true
                }
                R.id.ru_leave -> {
                    showLeaveChatDialog()
                    true
                }
                R.id.ru_report_group -> {
                    reportOnOwner()
                    true
                }
                R.id.mm_delete_chat -> {
                    showDeleteChatDialog()
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
                    shoReportMMUserDialog()
                    true
                }
                R.id.report_single -> {
                    showReportSingleDialog()
                    true
                }
                R.id.kick -> {
                    true
                }
                R.id.mm_delete_group_chat -> {
                    showDeleteChatDialog()
                    true
                }
                R.id.mm_block_user -> {
                    showMMBlockUserDialog()
                    true
                }
                R.id.mm_unblock_user -> {
                    showMMUnblockUserDialog()
                    true
                }
                else -> false
            }
        }
        return false
    }

    private fun showMMUnblockUserDialog() {
        chatWithUser.chat.members.firstOrNull { it.id != chatWithUser.user.id }?.let {
            BlockUserDialog.create(
                requireContext(),
                getString(R.string.chats_mm_unblock),
                getString(R.string.chats_mm_unblock_description),
                getString(R.string.chats_mm_unblock_button),
                getString(R.string.chats_mm_unblock_cancel)
            ) { viewModel.onUnblockUserClick(it) }.show()
        }
    }

    private fun showMMBlockUserDialog() {
        chatWithUser.chat.members.firstOrNull { it.id != chatWithUser.user.id }?.let {
            BlockUserDialog.create(
                requireContext(),
                getString(R.string.chats_mm_block),
                getString(R.string.chats_mm_block_description),
                getString(R.string.chats_mm_block_button),
                getString(R.string.chats_mm_block_cancel)
            ) { viewModel.onBlockUserClick(it.id) }.show()
        }
    }

    private fun showReportSingleDialog() {
        if (kickOrReportUserId > 0) {
            currentDialog = ReportAbuseDialog.create(
                requireContext(),
                {
                    currentDialog?.dismiss()
                    viewModel.onReport(it, kickOrReportUserId)
                }, {
                    currentDialog?.dismiss()
                    viewModel.onBlockUserClick(kickOrReportUserId)
                })
            currentDialog?.show()
        }
    }

    private fun shoReportMMUserDialog() {
        chatWithUser.chat.members.firstOrNull { it.id != chatWithUser.user.id }
            ?.let { member ->
                currentDialog = ReportAbuseDialog.create(
                    requireContext(), {
                        currentDialog?.dismiss()
                        viewModel.onReport(it, member.id)
                    }, {
                        currentDialog?.dismiss()
                        viewModel.onBlockUserClick(member.id)
                    })
                currentDialog?.show()
            }
    }

    private fun showDeleteChatDialog() {
        BlockUserDialog.create(
            requireContext(),
            getString(R.string.chats_delete),
            getString(R.string.chats_delete_description),
            getString(R.string.chats_delete_button),
            getString(R.string.chats_delete_cancel)
        ) { viewModel.onDeleteChat() }.show()
    }

    private fun showDeleteChannelDialog() {
        BlockUserDialog.create(
            requireContext(),
            getString(R.string.delete_this_channel),
            getString(R.string.delete_channel_description),
            getString(R.string.yes_delete),
            getString(R.string.chats_delete_cancel)
        ) { viewModel.onDeleteChat() }.show()
    }

    private fun showLeaveChatDialog() {
        BlockUserDialog.create(
            requireContext(),
            getString(R.string.chats_leave),
            getString(R.string.chats_leave_description),
            getString(R.string.chats_leave_button),
            getString(R.string.chats_leave_cancel)
        ) { viewModel.onLeave() }.show()
    }

    private fun showLeaveChannelDialog() {
        BlockUserDialog.create(
            requireContext(),
            getString(R.string.leave_channel),
            getString(R.string.leave_channel_description),
            getString(R.string.chats_leave_button),
            getString(R.string.chats_leave_cancel)
        ) { viewModel.onLeave() }.show()
    }

    private fun reportOnOwner() {
        context?.let { context ->
            currentDialog = ReportAbuseDialog.create(
                context,
                {
                    currentDialog?.dismiss()
                    viewModel.onReportChatOwner(it)
                }, {
                    currentDialog?.dismiss()
                    viewModel.onBlockUserClick(chatWithUser.chat.chatOwnerId)
                })
            currentDialog?.show()
        }
    }

    private fun createEditNameDialog(): AlertDialog {
        return EditChatNameDialog.create(
            requireContext(),
            chatWithUser.chat.title
        ) {
            if (hasConnection) {
                viewModel.updateChatName(it)
                currentDialog?.dismiss()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        fun getMimeType() = when (requestCode) {
            REQUEST_CODE_VIDEO -> MIME_TYPE_VIDEO
            REQUEST_CODE_FILE -> MIME_TYPE_ALL
            else -> MIME_TYPE_IMAGE
        }

        if (resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                lastFileUri = it
            }
            MediaValidator.executeIfUriSupported(requireContext(), lastFileUri,
                onError = {
                    when (it) {
                        MediaValidator.ValidationError.SIZE -> showDefaultError(getString(R.string.file_incorrect_size))
                        MediaValidator.ValidationError.FORMAT -> showDefaultError(getString(R.string.file_not_supported))
                    }
                },
                action = {
                    viewModel.sendMessage(
                        message = "",
                        attachmentType = getMimeType(),
                        attachmentUrl = lastFileUri.toString()
                    )
                })
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroyView() {
        messageList.adapter = null
        super.onDestroyView()
    }

    companion object {
        private const val REQUEST_CODE_CAMERA = 104
        private const val REQUEST_CODE_VIDEO = 105
        private const val REQUEST_CODE_GALLERY = 106
        private const val REQUEST_CODE_FILE = 107

        private const val IMAGE_FILENAME = "temp_image"

        private const val MIME_TYPE_IMAGE = "image/*"
        private const val MIME_TYPE_VIDEO = "video/*"
        private const val MIME_TYPE_ALL = "*/*"

        const val PRIVATE_CHAT_MEMBER_COUNT = 2 //server sent members count without you.
        const val CHAT_KEY = "chat"
        const val CHAT_TYPE = "chat_type"
        const val CHAT_USER = "chat_user"

        fun getInstance(
            chat: ChatEntity,
            user: UserEntity,
            chatType: ChatType = ChatType.CHAT
        ): ChatFragment {
            return ChatFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CHAT_KEY, chat)
                    putParcelable(CHAT_USER, user)
                    putString(CHAT_TYPE, chatType.toString())
                }
            }
        }
    }
}