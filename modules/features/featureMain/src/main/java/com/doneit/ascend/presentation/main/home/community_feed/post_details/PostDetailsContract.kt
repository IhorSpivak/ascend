package com.doneit.ascend.presentation.main.home.community_feed.post_details

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.presentation.main.home.community_feed.comments_view.CommentsViewContract

interface PostDetailsContract {
    interface ViewModel : CommentsViewContract.ViewModel {
        val currentPost: LiveData<Post>
        fun showUserDetails(userId: Long)
        fun onEditPostClick()
        fun onDeletePostClick()
        fun likePost()
        fun unlikePost()
        fun reportUser(reason: String)
        fun attachmentClicked(attachments: List<Attachment>, selected: Int)
    }

    interface Router : CommentsViewContract.Router {
        fun navigateToCreatePost(post: Post?)
        fun navigateToPreview(attachments: List<Attachment>, selected: Int)
    }
}