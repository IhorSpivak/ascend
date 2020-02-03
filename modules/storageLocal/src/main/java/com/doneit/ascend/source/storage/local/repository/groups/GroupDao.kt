package com.doneit.ascend.source.storage.local.repository.groups

import androidx.paging.DataSource
import androidx.room.*
import com.doneit.ascend.source.storage.local.data.GroupLocal

@Dao
interface GroupDao {

    @Query("SELECT * FROM groups WHERE status = :status or (:status is null and 1) ORDER BY start_time ASC")
    fun getAllASC(status: Int?): DataSource.Factory<Int, GroupLocal>

    @Query("SELECT * FROM groups WHERE status = :status or (:status is null and 1) ORDER BY start_time DESC")
    fun getAllDESC(status: Int?): DataSource.Factory<Int, GroupLocal>

    @Transaction
    @Query("SELECT * FROM groups WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): GroupLocal?

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(groups: List<GroupLocal>)

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(group: GroupLocal)

    @Delete
    suspend fun remove(group: GroupLocal)

    @Transaction
    @Query("DELETE FROM groups")
    suspend fun removeAll()
}