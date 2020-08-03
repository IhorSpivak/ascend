package com.doneit.ascend.presentation.main.home.community_feed

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.chats.ChatEntity
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.main.base.BaseViewModel

interface CommunityFeedContract {
    interface ViewModel : BaseViewModel {
        val posts: LiveData<PagedList<Post>>
        val channels: LiveData<PagedList<ChatEntity>>

        fun initUser(user: UserEntity)
        fun onEditPostClick(post: Post)
        fun onDeletePostClick(post: Post)
        fun onNewPostClick()
        fun onChannelClick(channel: ChatEntity)
        fun onSeeAllClick()
        fun leaveComment(postId: Long, message: String)
        fun likePost(postId: Long)
        fun unlikePost(postId: Long)
        fun onUserClick(userId: Long)
        fun newItem(post: Post)
        fun reportUser(reason: String, userId: Long)
        fun attachmentClicked(attachments: List<Attachment>, selected: Int)
        fun updateCommentsCount(postId: Long, commentsCount: Int)
    }

    interface Router {
        fun navigateToCreatePost(post: Post? = null)
        fun navigateToPreview(attachments: List<Attachment>, selected: Int)
        fun navigateToChannels()
        fun navigateToMMInfo(userId: Long)
    }
}