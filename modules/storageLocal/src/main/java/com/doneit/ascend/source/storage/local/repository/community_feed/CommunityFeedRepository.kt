package com.doneit.ascend.source.storage.local.repository.community_feed

import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.community_feed.PostWithAttachments

internal class CommunityFeedRepository(
    private val communityFeedDao: CommunityFeedDao
) : ICommunityFeedRepository {
    override fun getFeed(): DataSource.Factory<Int, PostWithAttachments> {
        return communityFeedDao.getFeed()
    }

    override suspend fun insertFeed(feed: List<PostWithAttachments>) {
        communityFeedDao.insertFeed(feed.map { it.postLocal })
        feed.map {
            communityFeedDao.insertAll(it.attachments)
        }
    }

    override suspend fun deleteAll() {
        communityFeedDao.deleteAll()
    }
}