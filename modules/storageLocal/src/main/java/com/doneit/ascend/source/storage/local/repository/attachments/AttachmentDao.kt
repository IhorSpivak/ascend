package com.doneit.ascend.source.storage.local.repository.attachments

import androidx.paging.DataSource
import androidx.room.*
import com.doneit.ascend.source.storage.local.data.AttachmentLocal

@Dao
interface AttachmentDao{

    @Query("SELECT * FROM attachments ORDER BY created_at")
    fun getAll(): DataSource.Factory<Int, AttachmentLocal>

    @Transaction
    @Query("SELECT * FROM attachments WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): AttachmentLocal?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(attachmentLocal: List<AttachmentLocal>)

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(attachmentLocal: AttachmentLocal)

    @Delete
    suspend fun remove(attachmentLocal: AttachmentLocal)

    @Transaction
    @Query("DELETE FROM attachments")
    suspend fun removeAll()
}