package com.doneit.ascend.source.storage.local.repository.chats

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.chat.ChatLocal

interface IMyChatsRepository {
    fun getList(): DataSource.Factory<Int, ChatLocal>
    fun getListLive(): LiveData<List<ChatLocal>>
    suspend fun insert(chat: ChatLocal)
    suspend fun insertAll(chats: List<ChatLocal>)
    suspend fun remove(id: Long)
    suspend fun removeAll()
}