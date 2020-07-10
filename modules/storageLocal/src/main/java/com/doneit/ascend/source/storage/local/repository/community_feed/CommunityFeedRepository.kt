package com.doneit.ascend.source.storage.local.repository.community_feed

import com.doneit.ascend.source.storage.local.data.community_feed.CommentLocal
import com.doneit.ascend.source.storage.local.data.community_feed.PostWithAttachments

internal class CommunityFeedRepository(
    private val communityFeedDao: CommunityFeedDao
) : ICommunityFeedRepository {
    override suspend fun getFeed(offset: Int, limit: Int): List<PostWithAttachments> {
        return communityFeedDao.getFeed(offset, limit)
    }

    override suspend fun insertFeed(feed: List<PostWithAttachments>) {
        communityFeedDao.insertFeed(feed.map { it.postLocal })
        feed.map {
            communityFeedDao.insertAll(it.attachments)
        }
    }

    override suspend fun insertPost(post: PostWithAttachments) {
        communityFeedDao.insert(post.postLocal)
        communityFeedDao.insertAll(post.attachments)
    }

    override suspend fun updatePost(post: PostWithAttachments) {
        communityFeedDao.updatePost(post.postLocal)
        communityFeedDao.updateAttachments(post.attachments)
    }

    override suspend fun getComments(offset: Int, limit: Int): List<CommentLocal> {
        return communityFeedDao.getComments(offset, limit)
    }

    override suspend fun insertComment(comment: CommentLocal) {
        communityFeedDao.insert(comment)
    }

    override suspend fun deleteComment(id: Long) {
        communityFeedDao.deleteCommentById(id)
    }

    override suspend fun insertComments(comments: List<CommentLocal>) {
        communityFeedDao.insertAllComments(comments)
    }

    override suspend fun deleteAll() {
        communityFeedDao.deleteAll()
    }
}