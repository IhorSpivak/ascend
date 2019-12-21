package com.doneit.ascend.domain.use_case.gateway

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.NotificationEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.NotificationListModel

interface INotificationGateway {
    suspend fun getNotificationList(request: NotificationListModel): PagedList<NotificationEntity>
    suspend fun delete(id: Long): ResponseEntity<Unit, List<String>>
}