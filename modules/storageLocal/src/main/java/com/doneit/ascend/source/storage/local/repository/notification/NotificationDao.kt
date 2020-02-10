package com.doneit.ascend.source.storage.local.repository.notification

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.doneit.ascend.source.storage.local.data.notification.NotificationLocal

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notification")
    fun getAll(): DataSource.Factory<Int, NotificationLocal>

    @Transaction
    @Query("SELECT * FROM notification WHERE is_read = :isRead")
    fun getRead(isRead: Boolean): DataSource.Factory<Int, NotificationLocal>

    @Transaction
    @Query("SELECT * FROM notification WHERE is_read = :isRead")
    fun getReadLive(isRead: Boolean): LiveData<List<NotificationLocal>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(masterMind: List<NotificationLocal>)

    @Transaction
    @Query("DELETE FROM notification WHERE id = :id")
    suspend fun remove(id: Long)

    @Transaction
    @Query("DELETE FROM notification")
    suspend fun removeAll()
}