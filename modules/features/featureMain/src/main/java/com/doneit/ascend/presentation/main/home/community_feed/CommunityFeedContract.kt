package com.doneit.ascend.presentation.main.home.community_feed

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface CommunityFeedContract {
    interface ViewModel : BaseViewModel {
        val posts: LiveData<PagedList<Post>>
        val channels: LiveData<PagedList<Channel>>

        fun onNewPostClick()
        fun onChannelClick(channel: Channel)
        fun onSeeAllClick()
        fun leaveComment(postId: Long, message: String)
        fun likePost(postId: Long)
        fun unlikePost(postId: Long)
        fun onUserClick(userId: Long)
    }

    interface Router {
        fun navigateToCreatePost()
    }
}