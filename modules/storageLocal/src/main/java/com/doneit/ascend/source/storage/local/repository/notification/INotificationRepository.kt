package com.doneit.ascend.source.storage.local.repository.notification

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.notification.NotificationLocal

interface INotificationRepository {
    fun getList(isRead: Boolean? = null): DataSource.Factory<Int, NotificationLocal>
    fun getListLive(isRead: Boolean = false): LiveData<List<NotificationLocal>>
    suspend fun insertAll(masterMinds: List<NotificationLocal>)
    suspend fun remove(id: Long)
    suspend fun removeAll()
}