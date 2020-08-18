package com.doneit.ascend.presentation.main.master_mind_info.mm_content.posts


import android.app.Activity
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
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.common.RvLazyAdapter
import com.doneit.ascend.presentation.dialog.DeleteDialog
import com.doneit.ascend.presentation.dialog.QuestionButtonType
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentMasterMindPostBinding
import com.doneit.ascend.presentation.main.home.channels.adapter.CommunityAdapter
import com.doneit.ascend.presentation.main.home.community_feed.CommunityFeedFragment
import com.doneit.ascend.presentation.main.home.community_feed.comments_view.CommentsViewBottomSheetFragment
import com.doneit.ascend.presentation.main.home.community_feed.common.MMPostClickListener
import com.doneit.ascend.presentation.main.home.community_feed.common.MMPostsAdapter
import com.doneit.ascend.presentation.main.home.community_feed.create_post.CreatePostFragment
import com.doneit.ascend.presentation.utils.extensions.hideKeyboard
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class MMPostsFragment : BaseFragment<FragmentMasterMindPostBinding>() {

    override val viewModel: MMPostsContract.ViewModel by instance()
    override val viewModelModule: Kodein.Module
        get() = MMPostsViewModelModule.get(this)

    private val newPostReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            context ?: return
            intent ?: return
            if (intent.action == CommunityFeedFragment.ACTION_NEW_POST) {
                viewModel.newItem(intent.getParcelableExtra(CreatePostFragment.RESULT)!!)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        hideKeyboard()
        activity?.currentFocus?.clearFocus()
    }

    private val communityAdapter by lazy {
        CommunityAdapter(
            onCommunitySelect = { community ->
                viewModel.getPostList(userId.toInt(),community.title.toLowerCase())
            }
        )
    }

    private val initPostsAdapter: MMPostsAdapter by RvLazyAdapter {
        MMPostsAdapter(
            postClickListeners(), user

        ) to { binding.rvPosts }
    }

    private val userId: Long by lazy {
        requireArguments().getLong(KEY_MM_ID)
    }

    private var currentDialog: AlertDialog? = null

    private val user by lazy {
        requireArguments().getParcelable<UserEntity>(KEY_USER)!!
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        context.registerReceiver(
            newPostReceiver,
            IntentFilter(CommunityFeedFragment.ACTION_NEW_POST)
        )
    }


    private fun postClickListeners(): MMPostClickListener {
        return MMPostClickListener(
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
                viewModel.onShareClick(it)
            },
            onCreatePostListener = viewModel::onNewPostClick,
            onSeeAllClickListener = viewModel::onSeeAllClick,
            onCommentClick = ::showComments,
            onMediaClick = viewModel::attachmentClicked
        )
    }


    private fun showComments(it: Long) {
        CommentsViewBottomSheetFragment.newInstance(
            it,
            requireArguments().getParcelable(KEY_USER)!!
        )
            .show(
                childFragmentManager,
                CommentsViewBottomSheetFragment::class.java.simpleName
            )
    }

    override fun viewCreated(savedInstanceState: Bundle?) {

        binding.apply {
            communityList.adapter = communityAdapter
            rvPosts.itemAnimator = null
        }
        viewModel.getPostList(userId.toInt(),"recovery")
        viewModel.initUser(user)
        initPostsAdapter
        viewModel.apply {
            communityList.observe(viewLifecycleOwner, Observer { communities ->
                communityAdapter.setData(communities)
            })
            fetchCommunityList()
        }


        observeData()
    }


    private fun observeData() {
        with(viewModel) {
            observe(posts, ::onPostsReceived)
        }
    }

    private fun onPostsReceived(posts: PagedList<Post>) {
        initPostsAdapter.submitList(posts)
    }

    override fun onDestroy() {
        requireContext().unregisterReceiver(newPostReceiver)
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data ?: return
        if (requestCode == CommentsViewBottomSheetFragment.REQUEST_CODE_COMMENTS && resultCode == Activity.RESULT_OK) {
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


    companion object {
        const val ACTION_NEW_POST = "NEW_POST"
        private const val KEY_USER = "USER"
        private const val KEY_MM_ID = "ID"
        fun newInstance(userId: Long, userEntity: UserEntity) = MMPostsFragment()
            .apply {
                arguments = Bundle().apply {
                    putLong(KEY_MM_ID, userId)
                    putParcelable(KEY_USER, userEntity)

                }
            }
    }
}