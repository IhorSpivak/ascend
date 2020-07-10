package com.doneit.ascend.source.storage.local.repository.community_feed

import androidx.room.*
import com.doneit.ascend.source.storage.local.data.community_feed.CommentLocal
import com.doneit.ascend.source.storage.local.data.community_feed.PostAttachmentLocal
import com.doneit.ascend.source.storage.local.data.community_feed.PostLocal
import com.doneit.ascend.source.storage.local.data.community_feed.PostWithAttachments

@Dao
interface CommunityFeedDao {
    @Query("SELECT * FROM post ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getFeed(offset: Int, limit: Int): List<PostWithAttachments>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeed(feed: List<PostLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(attachments: List<PostAttachmentLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostLocal)

    @Update
    suspend fun updatePost(post: PostLocal)

    @Update
    suspend fun updateAttachments(attachments: List<PostAttachmentLocal>)

    @Delete
    suspend fun delete(attachment: PostAttachmentLocal)

    @Query("DELETE FROM post")
    suspend fun deleteAll()

    @Query("SELECT * FROM comment ORDER BY createdAt DESC LIMIT :limit OFFSET :offset")
    suspend fun getComments(offset: Int, limit: Int): List<CommentLocal>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(comment: CommentLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllComments(comment: List<CommentLocal>)

    @Query("DELETE FROM comment WHERE id = :commentId")
    suspend fun deleteCommentById(commentId: Long)

    @Query("DELETE FROM post WHERE id = :postId")
    suspend fun deletePostById(postId: Long)
}