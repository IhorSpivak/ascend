package com.doneit.ascend.domain.use_case.interactor.community_feed

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
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
}