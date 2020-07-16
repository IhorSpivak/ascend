package com.doneit.ascend.source.storage.local.repository.chats

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.chat.BlockedUserLocal
import com.doneit.ascend.source.storage.local.data.chat.ChatWithLastMessage
import com.doneit.ascend.source.storage.local.data.chat.MemberLocal
import com.doneit.ascend.source.storage.local.data.chat.MessageWithPost

interface IMyChatsRepository {
    fun getList(title: String?): DataSource.Factory<Int, ChatWithLastMessage>
    fun getListLive(): LiveData<List<ChatWithLastMessage>>
    fun getMessageList(chatId: Long): DataSource.Factory<Int, MessageWithPost>
    fun getMessageListLive(): LiveData<List<MessageWithPost>>
    fun getMemberList(): DataSource.Factory<Int, MemberLocal>
    fun getMemberListLive(): LiveData<List<MemberLocal>>
    fun getBlockedUsersLive(query: String?): DataSource.Factory<Int, BlockedUserLocal>
    suspend fun insert(chat: ChatWithLastMessage)
    suspend fun insertAll(chats: List<ChatWithLastMessage>)
    suspend fun insertMessage(message: MessageWithPost)
    suspend fun insertAllMessages(messages: List<MessageWithPost>)
    suspend fun getLocalMessagesCount(): Int
    suspend fun insertMember(memberLocal: MemberLocal)
    suspend fun insertAllMembers(members: List<MemberLocal>)
    suspend fun remove(id: Long)
    suspend fun removeAll()
    suspend fun insertBlockedUser(blockedUserLocal: BlockedUserLocal)
    suspend fun insertAllBlockedUsers(blockedUsers: List<BlockedUserLocal>)
    suspend fun removeBlockedUser(id: Long)
    suspend fun removeAllBlockedUsers()
    suspend fun removeMessage(id: Long)
    suspend fun removeAllMessage()
    suspend fun removeMember(id: Long)
    suspend fun removeAllMembers()
    suspend fun markMessageAsRead(id: Long, status: String)
    suspend fun removeAllUnavailableChats(ids: List<Long>)
}