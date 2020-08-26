package com.doneit.ascend.source.storage.local.repository.chats

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.doneit.ascend.source.storage.local.data.UserLocal
import com.doneit.ascend.source.storage.local.data.chat.*
import com.doneit.ascend.source.storage.local.data.community_feed.PostAttachmentLocal
import com.doneit.ascend.source.storage.local.data.community_feed.PostLocal

@Dao
abstract class MyChatsDao {

    @Query("SELECT * FROM users LIMIT 1")
    abstract fun getUser(): UserLocal

    @Query("SELECT * FROM chat ORDER BY updatedAt DESC")
    abstract fun getAll(): DataSource.Factory<Int, ChatWithLastMessage>

    @Query("SELECT * FROM chat where title LIKE  :title order by updatedAt DESC")
    abstract fun getAllChatByTitle(title: String): DataSource.Factory<Int, ChatWithLastMessage>

    @Query("SELECT * FROM messages where chatId LIKE :chatId ORDER BY createdAt DESC")
    abstract fun getAllMessages(chatId: Long): DataSource.Factory<Int, MessageWithPost>

    @Query("SELECT COUNT(*) FROM messages")
    abstract fun getMessagesCount(): Int

    @Query("SELECT * FROM members ORDER BY fullName ASC")
    abstract fun getAllMembers(): DataSource.Factory<Int, MemberLocal>

    @Transaction
    @Query("SELECT * FROM chat ORDER BY updatedAt DESC")
    abstract fun getAllLive(): LiveData<List<ChatWithLastMessage>>

    @Transaction
    @Query("SELECT * FROM members ORDER BY fullName ASC")
    abstract fun getAllMembersLive(): LiveData<List<MemberLocal>>

    @Transaction
    @Query("SELECT * FROM messages ORDER BY createdAt DESC")
    abstract fun getAllMessagesLive(): LiveData<List<MessageWithPost>>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(chats: List<ChatLocal>)

    @Query("UPDATE messages SET status = :status WHERE id LIKE :id")
    abstract suspend fun markMessageAsRead(id: Long, status: String)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(chat: ChatLocal)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllMessages(chats: List<MessageLocal>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllMembers(member: List<MemberLocal>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMessage(message: MessageLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertPost(post: PostLocal)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllAttachments(attachments: List<PostAttachmentLocal>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertMember(member: MemberLocal)

    @Transaction
    @Query("DELETE FROM chat WHERE id = :id")
    abstract suspend fun remove(id: Long)

    @Transaction
    @Query("DELETE FROM messages WHERE id = :id")
    abstract suspend fun removeMessage(id: Long)

    @Transaction
    @Query("DELETE FROM chat WHERE id NOT IN (:ids)")
    abstract suspend fun removeUnavailableChats(ids: List<Long>)

    @Transaction
    @Query("DELETE FROM members WHERE id = :id")
    abstract suspend fun removeMember(id: Long)

    @Transaction
    @Query("DELETE FROM chat")
    abstract suspend fun removeAll()

    @Transaction
    @Query("DELETE FROM messages")
    abstract suspend fun removeAllMessages()

    @Transaction
    @Query("DELETE FROM members")
    abstract suspend fun removeAllMembers()

    @Query("SELECT * FROM blocked_users where fullName LIKE :query ORDER BY fullName ASC")
    abstract fun getAllBlockedUsers(query: String): DataSource.Factory<Int, BlockedUserLocal>

    @Transaction
    @Query("DELETE FROM blocked_users")
    abstract suspend fun removeAllBlockedUsers()

    @Transaction
    @Query("DELETE FROM blocked_users WHERE id = :id")
    abstract suspend fun removeBlockedUser(id: Long)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAllBlockedUsers(users: List<BlockedUserLocal>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertBlockedUser(user: BlockedUserLocal)

    @Transaction
    open suspend fun insertChatWithLastMessage(chat: ChatWithLastMessage) {
        insert(chat.chatLocal)
        chat.lastMessage?.let {
            insertMessageWithPost(chat.lastMessage)
        }
    }

    @Transaction
    open suspend fun insertAllChatsWithLastMessage(chats: List<ChatWithLastMessage>) {
        chats.forEach { chat ->
            insert(chat.chatLocal)
            chat.lastMessage?.let {
                insertMessageWithPost(chat.lastMessage)
            }
        }
    }

    @Transaction
    open suspend fun insertMessageWithPost(message: MessageWithPost) {
        if (message.messageLocal.userId == getUser().id) {
            removeMessage(-1L)
        }
        insertMessage(message.messageLocal)
        message.post?.let {
            insertPost(it.postLocal)
            insertAllAttachments(it.attachments)
        }

    }

    @Transaction
    open suspend fun insertAllMessageWithPost(messages: List<MessageWithPost>) {
        messages.forEach { message ->
            insertMessageWithPost(message)
        }
    }
}