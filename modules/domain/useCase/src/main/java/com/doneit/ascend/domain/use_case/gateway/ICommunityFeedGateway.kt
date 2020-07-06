package com.doneit.ascend.domain.use_case.gateway

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
import kotlinx.coroutines.CoroutineScope

interface ICommunityFeedGateway {
    fun loadPosts(
        scope: CoroutineScope,
        communityFeedDTO: CommunityFeedDTO
    ): LiveData<PagedList<Post>>

    fun loadChannels(
        scope: CoroutineScope,
        communityFeedDTO: CommunityFeedDTO
    ): LiveData<PagedList<Channel>>
}