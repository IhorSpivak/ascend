package com.doneit.ascend.domain.gateway.gateway.community_feed

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.doneit.ascend.domain.entity.community_feed.Post
import com.doneit.ascend.domain.entity.dto.CommunityFeedDTO
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.remote.repository.community_feed.ICommunityFeedRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CachedDataSource(
    private val coroutineScope: CoroutineScope,
    private val remote: ICommunityFeedRepository,
    private val local: com.doneit.ascend.source.storage.local.repository.community_feed.ICommunityFeedRepository,
    private val request: CommunityFeedDTO
) : PageKeyedDataSource<Int, Post>() {

    private val postsMap = mutableMapOf<Int, List<Post>>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Post>
    ) {
        coroutineScope.launch(Dispatchers.IO) {
            val result = remote.loadPosts(request.toRequest(1))
            if (result.isSuccessful) {
                val postsEntities = result.successModel!!.posts.orEmpty().map { it.toEntity() }
                postsMap[1] = postsEntities
                local.insertFeed(postsEntities.map { it.toLocal() })
                callback.onResult(
                    postsEntities,
                    null,
                    2
                )
            }
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Post>) {
        coroutineScope.launch(Dispatchers.IO) {
            val result = remote.loadPosts(request.toRequest(params.key))
            if (result.isSuccessful) {
                val posts = result.successModel!!.posts.orEmpty().map { it.toEntity() }
                local.insertFeed(posts.map { it.toLocal() })
                postsMap[params.key] = posts
                invalidate()
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Post>) {
        coroutineScope.launch(Dispatchers.IO) {
            val result = remote.loadPosts(request.toRequest(params.key))
            if (result.isSuccessful) {
                val posts = result.successModel!!.posts.orEmpty().map { it.toEntity() }
                local.insertFeed(posts.map { it.toLocal() })
                postsMap[params.key] = posts
                invalidate()
            }
        }
    }

    class Factory(
        private val coroutineScope: CoroutineScope,
        private val remote: ICommunityFeedRepository,
        private val local: com.doneit.ascend.source.storage.local.repository.community_feed.ICommunityFeedRepository,
        private val request: CommunityFeedDTO
    ) : DataSource.Factory<Int, Post>() {

        override fun create(): DataSource<Int, Post> {
            return CachedDataSource(
                coroutineScope = coroutineScope,
                remote = remote,
                local = local,
                request = request
            )
        }
    }
}