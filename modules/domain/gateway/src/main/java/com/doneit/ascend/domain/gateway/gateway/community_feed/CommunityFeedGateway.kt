package com.doneit.ascend.domain.gateway.gateway.community_feed

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.community_feed.Channel
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toEntity
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.boundaries.PostsBoundaryCallback
import com.doneit.ascend.domain.use_case.gateway.ICommunityFeedGateway
import com.doneit.ascend.source.storage.remote.repository.community_feed.ICommunityFeedRepository
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.Executors
import com.doneit.ascend.source.storage.local.repository.community_feed.ICommunityFeedRepository as ILocalCommunityFeedRepository

class CommunityFeedGateway(
    networkManager: NetworkManager,
    private val communityRemote: ICommunityFeedRepository,
    private val communityLocal: ILocalCommunityFeedRepository
) : BaseGateway(networkManager), ICommunityFeedGateway {
    override fun loadPosts(
        scope: CoroutineScope,
        communityFeedDTO: CommunityFeedDTO
    ): LiveData<PagedList<Post>> = liveData {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(communityFeedDTO.perPage ?: 10)
            .build()
        val factory = communityLocal.getFeed().map { it.toEntity() }
        val boundary = PostsBoundaryCallback(
            scope,
            communityRemote,
            communityLocal,
            communityFeedDTO
        )

        emitSource(
            LivePagedListBuilder(factory, config)
                .setFetchExecutor(Executors.newSingleThreadExecutor())
                .setBoundaryCallback(boundary)
                .build()
        )
        boundary.loadInitial()
    }

    override fun loadChannels(
        scope: CoroutineScope,
        communityFeedDTO: CommunityFeedDTO
    ): LiveData<PagedList<Channel>> = liveData {

    }

}