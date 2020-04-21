package com.doneit.ascend.source.storage.local.repository.chats

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.chat.ChatLocal
import com.doneit.ascend.source.storage.local.data.chat.MemberLocal
import com.doneit.ascend.source.storage.local.data.chat.MessageLocal

class MyChatsRepository(
    private val dao: MyChatsDao
) : IMyChatsRepository {
    override fun getList(): DataSource.Factory<Int, ChatLocal> {
        return dao.getAll()
    }

    override fun getListLive(): LiveData<List<ChatLocal>> {
        return dao.getAllLive()
    }

    override fun getMessageList(): DataSource.Factory<Int, MessageLocal> {
        return dao.getAllMessages()
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

}