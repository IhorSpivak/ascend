package com.doneit.ascend.domain.use_case.interactor.notification

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.NotificationEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.NotificationListModel
import com.doneit.ascend.domain.use_case.gateway.INotificationGateway

internal class NotificationInteractor(
    private val notificationGateway: INotificationGateway
) : NotificationUseCase {

    override suspend fun getNotificationList(request: NotificationListModel): PagedList<NotificationEntity> {
        return notificationGateway.getNotificationList(request)
    }

    override suspend fun delete(id: Long): ResponseEntity<Unit, List<String>> {
        return notificationGateway.delete(id)
    }
}