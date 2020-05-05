package com.doneit.ascend.source.storage.local.repository.chats

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.chat.BlockedUserLocal
import com.doneit.ascend.source.storage.local.data.chat.ChatLocal
import com.doneit.ascend.source.storage.local.data.chat.MemberLocal
import com.doneit.ascend.source.storage.local.data.chat.MessageLocal

class MyChatsRepository(
    private val dao: MyChatsDao
) : IMyChatsRepository {
    override fun getList(title: String?): DataSource.Factory<Int, ChatLocal> {
        return if (title.isNullOrEmpty()) {
            dao.getAll()
        } else {
            dao.getAllChatByTitle("%$title%")
        }
    }

    override fun getListLive(): LiveData<List<ChatLocal>> {
        return dao.getAllLive()
    }

    override fun getMessageList(chatId: Long): DataSource.Factory<Int, MessageLocal> {
        return dao.getAllMessages(chatId)
    }

    override fun getMessageListLive(): LiveData<List<MessageLocal>> {
        return dao.getAllMessagesLive()
    }

    override fun getMemberList(): DataSource.Factory<Int, MemberLocal> {
        return dao.getAllMembers()
    }

    override fun getMemberListLive(): LiveData<List<MemberLocal>> {
        return dao.getAllMembersLive()
    }

    override fun getBlockedUsersLive(query: String?): DataSource.Factory<Int, BlockedUserLocal> {
        return dao.getAllBlockedUsers("%$query%")
    }

    override suspend fun insert(chat: ChatLocal) {
        dao.insert(chat)
    }

    override suspend fun insertAll(chats: List<ChatLocal>) {
        dao.insertAll(chats)
    }

    override suspend fun insertMessage(message: MessageLocal) {
        dao.insertMessage(message)
    }

    override suspend fun insertAllMessages(messages: List<MessageLocal>) {
        dao.insertAllMessages(messages)
    }

    override suspend fun insertMember(memberLocal: MemberLocal) {
        dao.insertMember(memberLocal)
    }

    override suspend fun insertAllMembers(members: List<MemberLocal>) {
        dao.insertAllMembers(members)
    }

    override suspend fun remove(id: Long) {
        dao.remove(id)
    }

    override suspend fun removeAll() {
        dao.removeAll()
    }

    override suspend fun insertBlockedUser(blockedUserLocal: BlockedUserLocal) {
        dao.insertBlockedUser(blockedUserLocal)
    }

    override suspend fun insertAllBlockedUsers(blockedUsers: List<BlockedUserLocal>) {
        dao.insertAllBlockedUsers(blockedUsers)
    }

    override suspend fun removeBlockedUser(id: Long) {
        dao.removeBlockedUser(id)
    }

    override suspend fun removeAllBlockedUsers() {
        dao.removeAllBlockedUsers()
    }

    override suspend fun removeMessage(id: Long) {
        dao.removeMessage(id)
    }

    override suspend fun removeAllMessage() {
        dao.removeAllMessages()
    }

    override suspend fun removeMember(id: Long) {
        dao.removeMember(id)
    }

    override suspend fun removeAllMembers() {
        dao.removeAllMembers()
    }

    override suspend fun markMessageAsRead(id: Long, status: String) {
        dao.markMessageAsRead(id, status)
    }

    override suspend fun removeAllUnavailableChats(ids: List<Long>) {
        dao.removeUnavailableChats(ids)
    }

}