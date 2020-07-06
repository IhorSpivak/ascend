package com.doneit.ascend.domain.use_case.interactor.community_feed

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
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
}