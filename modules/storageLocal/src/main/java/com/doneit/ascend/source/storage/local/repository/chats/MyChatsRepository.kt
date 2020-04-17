package com.doneit.ascend.source.storage.local.repository.chats

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.chat.ChatLocal

class MyChatsRepository(
    private val dao: MyChatsDao
) : IMyChatsRepository {
    override fun getList(): DataSource.Factory<Int, ChatLocal> {
        return dao.getAll()
    }

    override fun getListLive(): LiveData<List<ChatLocal>> {
        return dao.getAllLive()
    }

    override suspend fun insert(chat: ChatLocal) {
        dao.insert(chat)
    }

    override suspend fun insertAll(chats: List<ChatLocal>) {
        dao.insertAll(chats)
    }

    override suspend fun remove(id: Long) {
        dao.remove(id)
    }

    override suspend fun removeAll() {
        dao.removeAll()
    }

}