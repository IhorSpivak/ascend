package com.doneit.ascend.presentation.main.master_mind_info.mm_content.posts

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.user.Community
import com.doneit.ascend.domain.entity.user.UserEntity
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.presentation.main.base.BaseViewModel
import com.doneit.ascend.presentation.main.home.community_feed.share_post.SharePostBottomSheetFragment

interface MMPostsContract {
    interface ViewModel : BaseViewModel {
        val posts: LiveData<PagedList<Post>>
        val communityList: LiveData<List<Community>>
        val user: UserEntity

        fun initUser(user: UserEntity)
        fun onEditPostClick(post: Post)
        fun onDeletePostClick(post: Post)
        fun onNewPostClick()
        fun getPostList(postId: Int, community: String)

        fun onSeeAllClick()
        fun leaveComment(postId: Long, message: String)
        fun likePost(postId: Long)
        fun unlikePost(postId: Long)
        fun onUserClick(userId: Long)
        fun newItem(post: Post)
        fun reportUser(reason: String, userId: Long)
        fun blockUser(userId: Long)
        fun attachmentClicked(attachments: List<Attachment>, selected: Int)
        fun updateCommentsCount(postId: Long, commentsCount: Int)
        fun fetchCommunityList()
        fun onShareClick(postId: Long)
    }

    interface Router {
        fun navigateToPreview(attachments: List<Attachment>, selected: Int)
        fun navigateToMMInfo(userId: Long)
        fun navigateToShare(
            id: Long,
            user: UserEntity,
            shareType: SharePostBottomSheetFragment.ShareType
        )
    }
}
