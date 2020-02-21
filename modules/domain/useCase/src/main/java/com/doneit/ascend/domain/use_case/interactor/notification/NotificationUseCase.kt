package com.doneit.ascend.domain.use_case.interactor.notification

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.NotificationListDTO
import com.doneit.ascend.domain.entity.notification.NotificationEntity

interface NotificationUseCase {
    fun getNotificationList(request: NotificationListDTO): LiveData<PagedList<NotificationEntity>>
    fun getUnreadLive(): LiveData<List<NotificationEntity>>
    suspend fun delete(id: Long): ResponseEntity<Unit, List<String>>

    fun notificationReceived(notification: NotificationEntity)
}