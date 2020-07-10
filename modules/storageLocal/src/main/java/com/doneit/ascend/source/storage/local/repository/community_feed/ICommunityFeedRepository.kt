package com.doneit.ascend.source.storage.local.repository.community_feed

import com.doneit.ascend.source.storage.local.data.community_feed.CommentLocal
import com.doneit.ascend.source.storage.local.data.community_feed.PostWithAttachments

interface ICommunityFeedRepository {
    suspend fun getFeed(offset: Int, limit: Int): List<PostWithAttachments>
    suspend fun insertFeed(feed: List<PostWithAttachments>)
    suspend fun insertPost(post: PostWithAttachments)
    suspend fun updatePost(post: PostWithAttachments)
    suspend fun getComments(offset: Int, limit: Int): List<CommentLocal>
    suspend fun insertComment(comment: CommentLocal)
    suspend fun insertComments(comments: List<CommentLocal>)
    suspend fun deleteComment(id: Long)
    suspend fun deletePost(id: Long)
    suspend fun deleteAll()
}