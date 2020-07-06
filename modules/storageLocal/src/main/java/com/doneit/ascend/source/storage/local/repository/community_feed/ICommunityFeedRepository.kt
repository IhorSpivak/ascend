package com.doneit.ascend.source.storage.local.repository.community_feed

import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.community_feed.PostWithAttachments

interface ICommunityFeedRepository {
    fun getFeed(): DataSource.Factory<Int, PostWithAttachments>
    suspend fun insertFeed(feed: List<PostWithAttachments>)
    suspend fun deleteAll()
}