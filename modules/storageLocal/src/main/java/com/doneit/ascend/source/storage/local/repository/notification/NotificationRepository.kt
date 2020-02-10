package com.doneit.ascend.source.storage.local.repository.notification

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.notification.NotificationLocal

internal class NotificationRepository(
    private val dao: NotificationDao
) : INotificationRepository {

    override fun getList(isRead: Boolean?): DataSource.Factory<Int, NotificationLocal> {
        return if(isRead == null) {
            dao.getAll()
        } else {
            dao.getRead(isRead)
        }
    }

    override fun getListLive(isRead: Boolean): LiveData<List<NotificationLocal>> {
        return dao.getReadLive(isRead)
    }

    override suspend fun insertAll(masterMinds: List<NotificationLocal>) {
        dao.insertAll(masterMinds)
    }

    override suspend fun remove(id: Long) {
        dao.remove(id)
    }

    override suspend fun removeAll() {
        dao.removeAll()
    }
}