package com.doneit.ascend.domain.gateway.gateway.boundaries

import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.remote.repository.community_feed.ICommunityFeedRepository
import com.vrgsoft.core.gateway.orZero
import kotlinx.coroutines.CoroutineScope
import com.doneit.ascend.source.storage.local.repository.community_feed.ICommunityFeedRepository as ILocalCommunityFeedRepository

class PostsBoundaryCallback(
    scope: CoroutineScope,
    private val remote: ICommunityFeedRepository,
    private val local: ILocalCommunityFeedRepository,
    private val request: CommunityFeedDTO
) : BaseBoundary<Post>(scope) {
    override suspend fun fetchPage() {
        val response = remote.loadPosts(request.toRequest(pageIndexToLoad))
        if (response.isSuccessful) {
            val model = response.successModel!!.posts.orEmpty().map { it.toEntity() }
            val loadedCount = model.size
            val remoteCount = response.successModel!!.count.orZero()
            receivedItems(loadedCount, remoteCount)
            local.insertFeed(model.map { it.toLocal() })
        }
    }
}