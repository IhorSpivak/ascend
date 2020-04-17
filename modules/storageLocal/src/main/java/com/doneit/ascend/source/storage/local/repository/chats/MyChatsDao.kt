package com.doneit.ascend.source.storage.local.repository.chats

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.doneit.ascend.source.storage.local.data.chat.ChatLocal

@Dao
interface MyChatsDao {

    //TODO: order_by last message time
    @Query("SELECT * FROM chat ORDER BY updatedAt DESC")
    fun getAll(): DataSource.Factory<Int, ChatLocal>

    @Transaction
    @Query("SELECT * FROM chat ORDER BY updatedAt DESC")
    fun getAllLive(): LiveData<List<ChatLocal>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chats: List<ChatLocal>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chat: ChatLocal)

    @Transaction
    @Query("DELETE FROM chat WHERE id = :id")
    suspend fun remove(id: Long)

    @Transaction
    @Query("DELETE FROM chat")
    suspend fun removeAll()
}