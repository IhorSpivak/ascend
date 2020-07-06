package com.doneit.ascend.presentation.main.home.community_feed

import android.os.Bundle
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.presentation.common.RvLazyAdapter
import com.doneit.ascend.presentation.main.base.BaseFragment
import com.doneit.ascend.presentation.main.databinding.FragmentCommunityFeedBinding
import com.doneit.ascend.presentation.main.home.community_feed.common.ChannelAdapter
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
            PostClickListeners(
                onUserClick = viewModel::onUserClick,
                onComplainClick = {},
                onLikeClick = viewModel::likePost,
                onOptionsClick = {},
                onSendCommentClick = viewModel::leaveComment,
                onShareClick = {}
            )
        ) to { binding.rvPosts }
    }
    private val initChannelAdapter: ChannelAdapter by RvLazyAdapter {
        ChannelAdapter(viewModel::onChannelClick) to { binding.rvChannels }
    }

    override fun viewCreated(savedInstanceState: Bundle?) {
        initPostsAdapter
        initChannelAdapter
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
        initChannelAdapter.submitList(channels)
    }

    companion object {
        fun newInstance() = CommunityFeedFragment()
    }
}