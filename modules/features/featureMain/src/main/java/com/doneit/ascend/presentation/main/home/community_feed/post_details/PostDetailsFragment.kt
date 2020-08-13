package com.doneit.ascend.presentation.main.home.community_feed.post_details

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import com.doneit.ascend.domain.entity.community_feed.Comment
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.common.RvLazyAdapter
import com.doneit.ascend.presentation.dialog.DeleteDialog
import com.doneit.ascend.presentation.dialog.QuestionButtonType
import com.doneit.ascend.presentation.dialog.ReportAbuseDialog
import com.doneit.ascend.presentation.main.R
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.common.gone
import com.doneit.ascend.presentation.main.databinding.FragmentPostDetailsBinding
import com.doneit.ascend.presentation.main.databinding.ViewPostContentBinding
import com.doneit.ascend.presentation.main.home.community_feed.comments_view.CommentsViewBottomSheetFragment
import com.doneit.ascend.presentation.main.home.community_feed.comments_view.common.CommentsAdapter
import com.doneit.ascend.presentation.main.home.community_feed.comments_view.common.CommentsClickListener
import com.doneit.ascend.presentation.main.home.community_feed.common.applyResizing
import com.doneit.ascend.presentation.main.home.community_feed.common.setupAttachments
import com.doneit.ascend.presentation.utils.applyFilter
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

class PostDetailsFragment : BaseFragment<FragmentPostDetailsBinding>() {
    override val viewModel: PostDetailsContract.ViewModel by instance()

    override val viewModelModule: Kodein.Module
        get() = PostDetailsViewModelModule.get(this)

    override val kodeinModule: Kodein.Module = Kodein.Module(this::class.java.simpleName) {
        bind<Post>() with provider {
            requireArguments().getParcelable<Post>(KEY_POST)!!
        }
    }

    private val commentsAdapter by RvLazyAdapter {
        CommentsAdapter(
            user,
            commentsClickListener(),
            doAfterListUpdated = ::scrollToStart
        ) to { binding.commentsView.rvComments }
    }

    private fun commentsClickListener(): CommentsClickListener {
        return CommentsClickListener(
            onUserClick = {
                viewModel.showUserDetails(it)
            },
            onDeleteClick = {
                viewModel.onDeleteComment(it)
            }
        )
    }

    private val user by lazy {
        requireArguments().getParcelable<UserEntity>(CommentsViewBottomSheetFragment.KEY_USER)!!
    }

    private val viewModelPost
        get() = requireNotNull(viewModel.currentPost.value)

    private var currentDialog: AlertDialog? = null

    override fun viewCreated(savedInstanceState: Bundle?) {
        commentsAdapter
        val post = requireArguments().getParcelable<Post>(KEY_POST)!!
        binding.apply {
            viewPostContent.setupAttachments(post.attachments)
            viewPostContent.applyResizing(post.attachments)
            viewPostContent.setClickListeners()
            if (post.isOwner) viewPostContent.btnBlock.gone()
            commentsView.model = viewModel
            model = viewModel
            commentsView.rvComments.itemAnimator = null
            commentsView.send.setOnClickListener {
                if (commentsView.message.text.toString().isNotBlank()) {
                    viewModel.leaveComment(commentsView.message.text.toString().trim())
                    commentsView.message.text.clear()
                }
            }
            commentsView.message.applyFilter()
            setupToolbar()
            observeData()
        }
    }

    private fun setupToolbar() {
        with(binding.postDetailsToolbar) {
            title = getString(R.string.view_post_label)
            setTitleTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    android.R.color.white
                )
            )
            setNavigationIcon(R.drawable.ic_back)
            setNavigationOnClickListener { activity?.onBackPressed() }
        }
    }

    private fun ViewPostContentBinding.setClickListeners() {
        mmiAvatar.setOnClickListener {
            viewModel.showUserDetails(viewModelPost.owner.id)
        }
        tvName.setOnClickListener {
            viewModel.showUserDetails(viewModelPost.owner.id)
        }
        btnLike.setOnClickListener {
            if (viewModelPost.isLikedMe) {
                viewModel.unlikePost()
            } else {
                viewModel.likePost()
            }
        }
        btnShare.setOnClickListener {
            viewModel.onSharePostClick(user)
        }
        btnBlock.setOnClickListener {
            if (viewModelPost.isOwner) {
                showSetting(it)
            } else {
                showAbuseDialog()
            }
        }
        imvFirst.setOnClickListener { viewModel.attachmentClicked(viewModelPost.attachments, 0) }
        imvSecond.setOnClickListener { viewModel.attachmentClicked(viewModelPost.attachments, 1) }
        imvThird.setOnClickListener { viewModel.attachmentClicked(viewModelPost.attachments, 2) }
        imvFourth.setOnClickListener { viewModel.attachmentClicked(viewModelPost.attachments, 3) }
        imvFifth.setOnClickListener { viewModel.attachmentClicked(viewModelPost.attachments, 4) }
    }

    private fun scrollToStart() {
        if (isResumed) {
            with(binding.commentsView.rvComments.layoutManager as LinearLayoutManager) {
                binding.commentsView.rvComments.doOnPreDraw {
                    scrollToPosition(0)
                }
            }
        }
    }

    private fun onGetComments(comments: PagedList<Comment>) {
        viewModel.setCommentsCount(comments.firstOrNull()?.postCommentsCount ?: 0)
        commentsAdapter.submitList(comments)
    }

    private fun observeData() {
        with(viewModel) {
            observe(comments, ::onGetComments)
        }
    }

    private fun showSetting(view: View) {
        PopupMenu(view.context, view, Gravity.START).apply {
            menuInflater.inflate(R.menu.post_menu, this.menu)
            menu.findItem(R.id.post_edit)?.isVisible =
                viewModel.currentPost.value!!.likesCount == 0 &&
                        viewModel.currentPost.value!!.commentsCount == 0
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.post_edit -> {
                        viewModel.onEditPostClick()
                        true
                    }
                    R.id.post_delete -> {
                        currentDialog = createDeleteDialog()
                        true
                    }
                    else -> false
                }
            }
        }.show()
    }

    private fun showAbuseDialog() {
        currentDialog = ReportAbuseDialog.create(
            requireContext()
        ) {
            currentDialog?.dismiss()
            viewModel.reportUser(it)
        }
        currentDialog?.show()
    }

    private fun createDeleteDialog(): AlertDialog {
        return DeleteDialog.create(
            requireContext(),
            getString(R.string.delete_post_title_dialog),
            R.string.delete_post_description_dialog,
            R.string.delete_post_ok,
            R.string.delete_post_cancel
        ) {
            currentDialog?.dismiss()
            when (it) {
                QuestionButtonType.POSITIVE -> {
                    activity?.onBackPressed()
                    viewModel.onDeletePostClick()
                }
            }
        }
    }

    companion object {

        private const val KEY_POST = "post_key"

        fun newInstance(user: UserEntity, post: Post) = PostDetailsFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_POST, post)
                putParcelable(CommentsViewBottomSheetFragment.KEY_USER, user)
            }
        }
    }

}