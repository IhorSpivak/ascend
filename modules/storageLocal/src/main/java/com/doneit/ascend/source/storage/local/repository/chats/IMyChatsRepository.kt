package com.doneit.ascend.source.storage.local.repository.chats

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.chat.ChatLocal
import com.doneit.ascend.source.storage.local.data.chat.MemberLocal
import com.doneit.ascend.source.storage.local.data.chat.MessageLocal

interface IMyChatsRepository {
    fun getList(title: String?): DataSource.Factory<Int, ChatLocal>
    fun getListLive(): LiveData<List<ChatLocal>>
    fun getMessageList(): DataSource.Factory<Int, MessageLocal>
    fun getMessageListLive(): LiveData<List<MessageLocal>>
    fun getMemberList(): DataSource.Factory<Int, MemberLocal>
    fun getMemberListLive(): LiveData<List<MemberLocal>>
    suspend fun insert(chat: ChatLocal)
    suspend fun insertAll(chats: List<ChatLocal>)
    suspend fun insertMessage(message: MessageLocal)
    suspend fun insertAllMessages(messages: List<MessageLocal>)
    suspend fun insertMember(memberLocal: MemberLocal)
    suspend fun insertAllMembers(members: List<MemberLocal>)
    suspend fun remove(id: Long)
    suspend fun removeAll()
    suspend fun removeMessage(id: Long)
    suspend fun removeAllMessage()
    suspend fun removeMember(id: Long)
    suspend fun removeAllMembers()
}