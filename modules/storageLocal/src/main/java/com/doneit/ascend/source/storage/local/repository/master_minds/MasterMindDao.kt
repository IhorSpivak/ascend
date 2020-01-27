package com.doneit.ascend.source.storage.local.repository.master_minds

import androidx.paging.DataSource
import androidx.room.*
import com.doneit.ascend.source.storage.local.data.MasterMindLocal

@Dao
interface MasterMindDao {

    @Query("SELECT * FROM master_minds")
    fun getAll(): DataSource.Factory<Int, MasterMindLocal>

    @Transaction
    @Query("SELECT * FROM master_minds WHERE id = :id LIMIT 1")
    suspend fun getById(id: Long): MasterMindLocal?

    @Query("SELECT * FROM master_minds WHERE followed = 1")
    fun getFollowed(): DataSource.Factory<Int, MasterMindLocal>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(masterMind: List<MasterMindLocal>)

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(masterMind: MasterMindLocal)

    @Delete
    suspend fun remove(masterMind: MasterMindLocal)

    @Transaction
    @Query("DELETE FROM master_minds")
    suspend fun removeAll()
}