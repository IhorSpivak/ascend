package com.doneit.ascend.domain.use_case.interactor.community_feed

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.community_feed.*
import com.doneit.ascend.domain.entity.dto.CommentsDTO
import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
import com.doneit.ascend.domain.entity.dto.SharePostDTO
import com.doneit.ascend.domain.use_case.PagedList
import kotlinx.coroutines.CoroutineScope

interface CommunityFeedUseCase {
    val commentStream: LiveData<CommunityFeedSocketEntity?>

    fun connectToChannel(community: String)

    fun disconnect()

    fun loadPosts(
        coroutineScope: CoroutineScope,
        feedRequest: CommunityFeedDTO
    ): LiveData<PagedList<Post>>

    fun loadChannels(
        coroutineScope: CoroutineScope,
        channelRequest: CommunityFeedDTO
    ): LiveData<PagedList<Channel>>

    fun likePost(
        coroutineScope: CoroutineScope,
        postId: Long,
        baseCallback: BaseCallback<Unit>
    )

    fun unlikePost(
        coroutineScope: CoroutineScope,
        postId: Long,
        baseCallback: BaseCallback<Unit>
    )

    fun createPostComment(
        coroutineScope: CoroutineScope,
        postId: Long,
        postComment: String,
        baseCallback: BaseCallback<Comment>
    )

    fun createPost(
        coroutineScope: CoroutineScope,
        description: String,
        attachments: List<Attachment>,
        baseCallback: BaseCallback<Post>
    )

    fun loadComments(
        coroutineScope: CoroutineScope,
        postId: Long,
        commentsRequest: CommentsDTO
    ): LiveData<PagedList<Comment>>

    fun deleteComment(
        coroutineScope: CoroutineScope,
        postId: Long,
        commentId: Long,
        baseCallback: BaseCallback<Unit>
    )

    fun deletePost(
        coroutineScope: CoroutineScope,
        postId: Long,
        baseCallback: BaseCallback<Unit>
    )

    fun updatePost(
        coroutineScope: CoroutineScope,
        postId: Long,
        description: String,
        deletedAttachments: Array<Long>,
        attachments: List<Attachment>,
        baseCallback: BaseCallback<Post>
    )

    fun sharePost(
        coroutineScope: CoroutineScope,
        postId: Long,
        sharePostDTO: SharePostDTO,
        baseCallback: BaseCallback<Unit>
    )
}