package com.doneit.ascend.domain.use_case.interactor.community_feed

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
import com.doneit.ascend.domain.use_case.PagedList
import com.doneit.ascend.domain.use_case.gateway.ICommunityFeedGateway
import kotlinx.coroutines.CoroutineScope

class CommunityFeedInteractor(
    private val gateway: ICommunityFeedGateway
) : CommunityFeedUseCase {
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
        baseCallback: BaseCallback<Unit>
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
}