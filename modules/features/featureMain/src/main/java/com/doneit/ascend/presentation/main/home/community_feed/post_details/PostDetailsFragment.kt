package com.doneit.ascend.presentation.main.home.community_feed.post_details

import android.os.Bundle
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.LinearLayoutManager
import com.doneit.ascend.domain.entity.community_feed.Comment
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.common.RvLazyAdapter
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentPostDetailsBinding
import com.doneit.ascend.presentation.main.home.community_feed.comments_view.CommentsViewBottomSheetFragment
import com.doneit.ascend.presentation.main.home.community_feed.comments_view.common.CommentsAdapter
import com.doneit.ascend.presentation.main.home.community_feed.comments_view.common.CommentsClickListener
import com.doneit.ascend.presentation.main.home.community_feed.common.applyResizing
import com.doneit.ascend.presentation.main.home.community_feed.common.setupAttachments
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
            post
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
            onUserClick = {},
            onDeleteClick = {
                viewModel.onDeleteComment(it)
            }
        )
    }

    private val user by lazy {
        requireArguments().getParcelable<UserEntity>(CommentsViewBottomSheetFragment.KEY_USER)!!
    }

    private val post by lazy {
        requireArguments().getParcelable<Post>(KEY_POST)!!
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        commentsAdapter
        binding.apply {
            viewPostContent.setupAttachments(post.attachments)
            viewPostContent.applyResizing(post.attachments)
            commentsView.model = viewModel
            postModel = post
            commentsView.rvComments.itemAnimator = null
            commentsView.send.setOnClickListener {
                if (commentsView.message.text.toString().isNotBlank()) {
                    viewModel.leaveComment(commentsView.message.text.toString())
                    commentsView.message.text.clear()
                }
            }
            observeData()
        }
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