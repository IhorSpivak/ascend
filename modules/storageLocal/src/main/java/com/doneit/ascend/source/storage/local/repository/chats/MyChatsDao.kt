package com.doneit.ascend.source.storage.local.repository.chats

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.doneit.ascend.source.storage.local.data.chat.ChatLocal
import com.doneit.ascend.source.storage.local.data.chat.MemberLocal
import com.doneit.ascend.source.storage.local.data.chat.MessageLocal

@Dao
interface MyChatsDao {

    @Query("SELECT * FROM chat ORDER BY last_message_updatedAt DESC, updatedAt DESC")
    fun getAll(): DataSource.Factory<Int, ChatLocal>

    @Query("SELECT * FROM chat where title LIKE  :title order by last_message_updatedAt DESC, updatedAt DESC")
    fun getAllChatByTitle(title: String): DataSource.Factory<Int, ChatLocal>

    @Query("SELECT * FROM messages where chatId LIKE :chatId ORDER BY updatedAt ASC")
    fun getAllMessages(chatId: Long): DataSource.Factory<Int, MessageLocal>

    @Query("SELECT * FROM members ORDER BY fullName ASC")
    fun getAllMembers(): DataSource.Factory<Int, MemberLocal>

    @Transaction
    @Query("SELECT * FROM chat ORDER BY last_message_updatedAt DESC, updatedAt DESC")
    fun getAllLive(): LiveData<List<ChatLocal>>

    @Transaction
    @Query("SELECT * FROM members ORDER BY fullName ASC")
    fun getAllMembersLive(): LiveData<List<MemberLocal>>

    @Transaction
    @Query("SELECT * FROM messages ORDER BY updatedAt DESC")
    fun getAllMessagesLive(): LiveData<List<MessageLocal>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(chats: List<ChatLocal>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(chat: ChatLocal)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMessages(chats: List<MessageLocal>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMembers(member: List<MemberLocal>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: MessageLocal)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMember(member: MemberLocal)

    @Transaction
    @Query("DELETE FROM chat WHERE id = :id")
    suspend fun remove(id: Long)

    @Transaction
    @Query("DELETE FROM messages WHERE id = :id")
    suspend fun removeMessage(id: Long)

    @Transaction
    @Query("DELETE FROM members WHERE id = :id")
    suspend fun removeMember(id: Long)

    @Transaction
    @Query("DELETE FROM chat")
    suspend fun removeAll()

    @Transaction
    @Query("DELETE FROM messages")
    suspend fun removeAllMessages()

    @Transaction
    @Query("DELETE FROM members")
    suspend fun removeAllMembers()
}