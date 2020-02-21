package com.doneit.ascend.domain.use_case.interactor.notification

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.NotificationListDTO
import com.doneit.ascend.domain.entity.notification.NotificationEntity
import com.doneit.ascend.domain.use_case.gateway.INotificationGateway

internal class NotificationInteractor(
    private val notificationGateway: INotificationGateway
) : NotificationUseCase {

    override fun getNotificationList(request: NotificationListDTO): LiveData<PagedList<NotificationEntity>> {
        return notificationGateway.getNotificationList(request)
    }

    override fun getUnreadLive(): LiveData<List<NotificationEntity>> {
        return notificationGateway.getUnreadLive()
    }

    override suspend fun delete(id: Long): ResponseEntity<Unit, List<String>> {
        return notificationGateway.delete(id)
    }

    override fun notificationReceived(notification: NotificationEntity) {
        notificationGateway.notificationReceived(notification)
    }
}