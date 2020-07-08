package com.doneit.ascend.source.storage.local.repository.community_feed

import com.doneit.ascend.source.storage.local.data.community_feed.PostWithAttachments

interface ICommunityFeedRepository {
    suspend fun getFeed(offset: Int, limit: Int): List<PostWithAttachments>
    suspend fun insertFeed(feed: List<PostWithAttachments>)
    suspend fun insertPost(post: PostWithAttachments)
    suspend fun updatePost(post: PostWithAttachments)
    suspend fun deleteAll()
}