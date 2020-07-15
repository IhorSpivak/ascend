package com.doneit.ascend.domain.use_case.interactor.community_feed

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.AttendeeEntity
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.domain.entity.community_feed.Comment
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.dto.CommentsDTO
import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
import com.doneit.ascend.domain.entity.dto.SharePostDTO
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.domain.use_case.gateway.ICommunityFeedGateway
import kotlinx.coroutines.CoroutineScope

class CommunityFeedInteractor(
    private val gateway: ICommunityFeedGateway
) : CommunityFeedUseCase {
    override val commentStream = gateway.commentStream


    override fun connectToChannel(community: String) {
        gateway.connectToChannel(community)
    }

    override fun disconnect() {
        gateway.disconnect()
    }

    override fun loadPosts(
        coroutineScope: CoroutineScope,
        feedRequest: CommunityFeedDTO
    ): LiveData<PagedList<Post>> {
        return gateway.loadPosts(coroutineScope, feedRequest)
    }

    override fun loadChannels(
        coroutineScope: CoroutineScope,
        channelRequest: CommunityFeedDTO
    ): LiveData<PagedList<Channel>> {
        return gateway.loadChannels(coroutineScope, channelRequest)
    }

    override fun likePost(
        coroutineScope: CoroutineScope,
        postId: Long,
        baseCallback: BaseCallback<Unit>
    ) {
        gateway.likePost(coroutineScope, postId, baseCallback)
    }

    override fun unlikePost(
        coroutineScope: CoroutineScope,
        postId: Long,
        baseCallback: BaseCallback<Unit>
    ) {
        gateway.unlikePost(coroutineScope, postId, baseCallback)
    }

    override fun createPostComment(
        coroutineScope: CoroutineScope,
        postId: Long,
        postComment: String,
        baseCallback: BaseCallback<Comment>
    ) {
        gateway.createPostComment(coroutineScope, postId, postComment, baseCallback)
    }

    override fun createPost(
        coroutineScope: CoroutineScope,
        description: String,
        attachments: List<Attachment>,
        baseCallback: BaseCallback<Post>
    ) {
        return gateway.createPost(coroutineScope, description, attachments, baseCallback)
    }

    override fun loadComments(
        coroutineScope: CoroutineScope,
        postId: Long,
        commentsRequest: CommentsDTO
    ): LiveData<PagedList<Comment>> {
        return gateway.loadComments(coroutineScope, postId, commentsRequest)
    }

    override fun deleteComment(
        coroutineScope: CoroutineScope,
        postId: Long,
        commentId: Long,
        baseCallback: BaseCallback<Unit>
    ) {
        return gateway.deleteComment(coroutineScope, postId = postId, commentId = commentId, baseCallback = baseCallback)
    }

    override fun deletePost(
        coroutineScope: CoroutineScope,
        postId: Long,
        baseCallback: BaseCallback<Unit>
    ) {
        return gateway.deletePost(coroutineScope, postId = postId, baseCallback = baseCallback)
    }

    override fun updatePost(
        coroutineScope: CoroutineScope,
        postId: Long,
        description: String,
        deletedAttachments: Array<Long>,
        attachments: List<Attachment>,
        baseCallback: BaseCallback<Post>
    ) {
        return gateway.updatePost(
            coroutineScope,
            postId,
            description,
            deletedAttachments,
            attachments,
            baseCallback
        )
    }

    override fun sharePost(
        coroutineScope: CoroutineScope,
        postId: Long,
        sharePostDTO: SharePostDTO,
        baseCallback: BaseCallback<Unit>
    ) {
        return gateway.sharePost(
            coroutineScope,
            postId,
            sharePostDTO,
            baseCallback
        )
    }

    override fun getUsersListPaged(
        coroutineScope: CoroutineScope,
        query: String,
        userId: Long
    ): androidx.paging.PagedList<AttendeeEntity> {
        return gateway.getUsersPagedList(coroutineScope, query, userId, null)
    }
}