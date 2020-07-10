package com.doneit.ascend.domain.use_case.interactor.community_feed

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.community_feed.Attachment
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
import com.doneit.ascend.domain.use_case.PagedList
import kotlinx.coroutines.CoroutineScope

interface CommunityFeedUseCase {
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
        baseCallback: BaseCallback<Unit>
    )

    fun createPost(
        coroutineScope: CoroutineScope,
        description: String,
        attachments: List<Attachment>,
        baseCallback: BaseCallback<Post>
    )
}