package com.doneit.ascend.presentation.main.home.community_feed

import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.MainActivityListener
import com.doneit.ascend.presentation.common.RvLazyAdapter
import com.doneit.ascend.presentation.dialog.DeleteDialog
import com.doneit.ascend.presentation.dialog.QuestionButtonType
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentCommunityFeedBinding
import com.doneit.ascend.presentation.main.home.community_feed.comments_view.CommentsViewBottomSheetFragment
import com.doneit.ascend.presentation.main.home.community_feed.common.PostClickListeners
import com.doneit.ascend.presentation.main.home.community_feed.common.PostsAdapter
import com.doneit.ascend.presentation.main.home.community_feed.create_post.CreatePostFragment
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.dialog_bottom_sheet_channels.view.*
import org.kodein.di.Kodein
import org.kodein.di.generic.instance
import java.util.*

class CommunityFeedFragment : BaseFragment<FragmentCommunityFeedBinding>() {
    override val viewModel: CommunityFeedContract.ViewModel by instance()

    override val viewModelModule: Kodein.Module
        get() = CommunityFeedViewModelModule.get(this)

    private val newPostReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            context ?: return
            intent ?: return
            if (intent.action == ACTION_NEW_POST) {
                viewModel.newItem(intent.getParcelableExtra(CreatePostFragment.RESULT)!!)
            }
        }
    }

    var listener: MainActivityListener? = null

    override fun onResume() {
        super.onResume()
        listener?.apply {
            setSearchEnabled(true)
            setFilterEnabled(false)
            setChatEnabled(true)
            setShareEnabled(false)
            setShareInAppEnabled(false)
            getUnreadMessageCount()
        }
        hideKeyboard()
        activity?.currentFocus?.clearFocus()
    }

    private val initPostsAdapter: PostsAdapter by RvLazyAdapter {
        PostsAdapter(
            postClickListeners(),
            viewModel.user.value!!
        ) to { binding.rvPosts }
    }

    private var currentDialog: AlertDialog? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = (context as MainActivityListener)
        context.registerReceiver(newPostReceiver, IntentFilter(ACTION_NEW_POST))
    }

    private fun postClickListeners(): PostClickListeners {
        return PostClickListeners(
            onUserClick = viewModel::onUserClick,
            onComplainClick = ::showAbuseDialog,
            onLikeClick = { liked, id, _ ->
                if (!liked)
                    viewModel.likePost(id)
                else viewModel.unlikePost(id)
            },
            onOptionsClick = ::showSetting,
            onSendCommentClick = { id, text, _ -> viewModel.leaveComment(id, text) },
            onShareClick = {
                viewModel.onSharePostClick(it)
            },
            onCreatePostListener = viewModel::onNewPostClick,
            onSeeAllClickListener = viewModel::onSeeAllClick,
            onChannelClick = ::handleChatNavigation,
            onCommentClick = ::showComments,
            onMediaClick = viewModel::attachmentClicked
        )
    }

    private fun showComments(it: Long) {
        CommentsViewBottomSheetFragment.newInstance(
            it,
            viewModel.user.value!!
        )
            .show(
                childFragmentManager,
                CommentsViewBottomSheetFragment::class.java.simpleName
            )
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        viewModel.user.observe(this, Observer {
            it ?: return@Observer
            viewModel.initUser(it)
        })
        viewModel.community.observe(this, Observer {
            setTitle(it)
            initPostsAdapter
            binding.rvPosts.itemAnimator = null
            observeData()
        })

    }

    private fun setTitle(community: String?) {
        var title = getString(R.string.main_title)
        community?.let {
            title = it
        }
        listener?.setCommunityTitle(title.toUpperCase(Locale.getDefault()))
    }


    private fun observeData() {
        with(viewModel) {
            observe(posts, ::onPostsReceived)
            observe(channels, ::onChannelsReceived)
        }
    }

    private fun onPostsReceived(posts: PagedList<Post>) {
        initPostsAdapter.submitList(posts)
    }

    private fun onChannelsReceived(channels: PagedList<ChatEntity>) {
        initPostsAdapter.submitChannels(channels)
    }

    override fun onDestroy() {
        requireContext().unregisterReceiver(newPostReceiver)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data ?: return
        if (requestCode == CommentsViewBottomSheetFragment.REQUEST_CODE_COMMENTS && resultCode == RESULT_OK) {
            val postId = data.getLongExtra(
                CommentsViewBottomSheetFragment.KEY_POST_ID,
                0
            )
            val commentsCount = data.getIntExtra(
                CommentsViewBottomSheetFragment.KEY_COMMENTS_COUNT,
                0
            )
            viewModel.updateCommentsCount(postId, commentsCount)
        }
    }

    private fun showSetting(view: View, post: Post) {
        PopupMenu(view.context, view, Gravity.START).apply {
            menuInflater.inflate(R.menu.post_menu, this.menu)
            menu.findItem(R.id.post_edit)?.isVisible =
                post.likesCount == 0 && post.commentsCount == 0
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.post_edit -> {
                        viewModel.onEditPostClick(post)
                        true
                    }
                    R.id.post_delete -> {
                        currentDialog = createDeleteDialog(post)
                        true
                    }
                    else -> false
                }
            }
        }.show()
    }

    private fun showAbuseDialog(userId: Long) {
        currentDialog = ReportAbuseDialog.create(
            requireContext()
        ) {
            currentDialog?.dismiss()
            viewModel.reportUser(it, userId)
        }
        currentDialog?.show()
    }

    private fun createDeleteDialog(post: Post): AlertDialog {
        return DeleteDialog.create(
            requireContext(),
            getString(R.string.delete_post_title_dialog),
            R.string.delete_post_description_dialog,
            R.string.delete_post_ok,
            R.string.delete_post_cancel
        ) {
            currentDialog?.dismiss()
            when (it) {
                QuestionButtonType.POSITIVE -> viewModel.onDeletePostClick(post)
            }
        }
    }

    private fun handleChatNavigation(channel: ChatEntity) {
        if (channel.chatOwnerId == viewModel.user.value!!.id) {
            viewModel.onChannelClick(channel)
        }
        when (channel.isSubscribed) {
            true -> viewModel.onChannelClick(channel)
            false -> showChannelsDialogInfo(channel)
        }
    }

    private fun showChannelsDialogInfo(channel: ChatEntity) {
        val view = layoutInflater.inflate(R.layout.dialog_bottom_sheet_channels, null)
        val dialog = BottomSheetDialog(context!!)
        dialog.setContentView(view)
        view.titleChannel.text = channel.title
        when (channel.isPrivate) {
            true -> view.channelType.text = resources.getString(R.string.private_channel)
            false -> view.channelType.text = resources.getString(R.string.public_channel)
        }
        view.user_name.text = channel.owner?.fullName
        view.descriptionChannel.text = channel.description
        view.qtyMembers.text = context?.getString(R.string.value_members, channel.membersCount)
        Glide.with(this)
            .load(channel.image?.url)
            .apply(RequestOptions.circleCropTransform())
            .into(view.channelImage)

        Glide.with(this)
            .load(channel.owner?.image?.url)
            .apply(RequestOptions.circleCropTransform())
            .into(view.userIcon)

        view.btn_join.setOnClickListener {
            dialog.dismiss()
            viewModel.onJoinChannel(channel)
        }
        dialog.show()
    }

    companion object {
        const val ACTION_NEW_POST = "NEW_POST"
        private const val KEY_USER = "USER"
        fun newInstance(user: UserEntity) = CommunityFeedFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_USER, user)
            }
        }
    }
}