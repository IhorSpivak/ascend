package com.doneit.ascend.source.storage.local.repository.community_feed

import androidx.paging.DataSource
import androidx.room.*
import com.doneit.ascend.source.storage.local.data.community_feed.PostAttachmentLocal
import com.doneit.ascend.source.storage.local.data.community_feed.PostLocal
import com.doneit.ascend.source.storage.local.data.community_feed.PostWithAttachments

@Dao
interface CommunityFeedDao {
    @Query("SELECT * FROM post ORDER BY updatedAt DESC")
    fun getFeed(): DataSource.Factory<Int, PostWithAttachments>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeed(feed: List<PostLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(attachments: List<PostAttachmentLocal>)

    @Delete
    suspend fun delete(attachment: PostAttachmentLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostLocal)

    @Delete
    suspend fun delete(post: PostLocal)

    @Query("DELETE FROM post")
    suspend fun deleteAll()
}