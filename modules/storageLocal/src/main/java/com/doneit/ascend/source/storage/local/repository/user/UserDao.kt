package com.doneit.ascend.source.storage.local.repository.user

import androidx.lifecycle.LiveData
import androidx.room.*
import com.doneit.ascend.source.storage.local.data.UserLocal

@Dao
interface UserDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserLocal)

    @Transaction
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: UserLocal)

    @Transaction
    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getFirstUser(): UserLocal?

    @Transaction
    @Query("SELECT * FROM users LIMIT 1")
    fun getFirstUserLive(): LiveData<UserLocal?>

    @Transaction
    @Query("DELETE FROM users")
    suspend fun remove()
}