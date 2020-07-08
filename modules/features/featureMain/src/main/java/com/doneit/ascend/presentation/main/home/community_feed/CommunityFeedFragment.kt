package com.doneit.ascend.presentation.main.home.community_feed

import android.os.Bundle
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.common.RvLazyAdapter
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentCommunityFeedBinding
import com.doneit.ascend.presentation.main.home.community_feed.common.PostClickListeners
import com.doneit.ascend.presentation.main.home.community_feed.common.PostsAdapter
import org.kodein.di.Kodein
import org.kodein.di.generic.instance

class CommunityFeedFragment : BaseFragment<FragmentCommunityFeedBinding>() {
    override val viewModel: CommunityFeedContract.ViewModel by instance()

    override val viewModelModule: Kodein.Module
        get() = CommunityFeedViewModelModule.get(this)

    private val initPostsAdapter: PostsAdapter by RvLazyAdapter {
        PostsAdapter(
            postClickListeners(),
            requireArguments().getParcelable(KEY_USER)!!
        ) to { binding.rvPosts }
    }

    private fun postClickListeners(): PostClickListeners {
        return PostClickListeners(
            onUserClick = viewModel::onUserClick,
            onComplainClick = {},
            onLikeClick = { liked, id, _ ->
                if (!liked)
                    viewModel.likePost(id)
                else viewModel.unlikePost(id)
            },
            onOptionsClick = {},
            onSendCommentClick = { id, text, _ -> viewModel.leaveComment(id, text) },
            onShareClick = {},
            onCreatePostListener = viewModel::onNewPostClick,
            onSeeAllClickListener = viewModel::onSeeAllClick,
            onChannelClick = viewModel::onChannelClick
        )
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        initPostsAdapter
        binding.rvPosts.itemAnimator = null
        observeData()
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

    private fun onChannelsReceived(channels: PagedList<Channel>) {
        initPostsAdapter.submitChannels(channels)
    }

    companion object {
        private const val KEY_USER = "USER"
        fun newInstance(user: UserEntity) = CommunityFeedFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_USER, user)
            }
        }
    }
}