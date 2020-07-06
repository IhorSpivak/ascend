package com.doneit.ascend.presentation.main.home.community_feed

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface CommunityFeedContract {
    interface ViewModel : BaseViewModel {
        val posts: LiveData<PagedList<Post>>
        val channels: LiveData<PagedList<Channel>>
        val user: LiveData<UserEntity>

        fun onNewPostClick()
        fun onChannelClick(channel: Channel)
        fun onSeeAllClick()
        fun leaveComment(postId: Long, message: String)
        fun likePost(postId: Long)
        fun onUserClick(userId: Long)
    }

    interface Router
}