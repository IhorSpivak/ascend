package com.doneit.ascend.presentation.main.home.community_feed.post_details

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.community_feed.PostNullable
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.presentation.main.home.community_feed.comments_view.CommentsViewContract
import com.doneit.ascend.presentation.main.home.community_feed.share_post.SharePostBottomSheetFragment

interface PostDetailsContract {
    interface ViewModel : CommentsViewContract.ViewModel {
        val currentPost: LiveData<Post>
        val currentPostNullable: LiveData<PostNullable>
        fun showUserDetails(userId: Long)
        fun onEditPostClick()
        fun onDeletePostClick()
        fun likePost()
        fun unlikePost()
        fun reportUser(reason: String)
        fun blockUser()
        fun attachmentClicked(attachments: List<Attachment>, selected: Int)
        fun onSharePostClick(user: UserEntity)
    }

    interface Router : CommentsViewContract.Router {
        fun navigateToCreatePost(post: Post?)
        fun navigateToPreview(attachments: List<Attachment>, selected: Int)
        fun navigateToShare(
            id: Long,
            user: UserEntity,
            shareType: SharePostBottomSheetFragment.ShareType
        )
    }
}