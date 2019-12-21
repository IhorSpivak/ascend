package com.doneit.ascend.source.storage.remote.repository.notification

import com.doneit.ascend.source.storage.remote.data.request.NotificationListRequest
import com.doneit.ascend.source.storage.remote.data.response.NotificationsListResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface INotificationRepository {
    suspend fun getAllNotifications(request: NotificationListRequest): RemoteResponse<NotificationsListResponse, ErrorsListResponse>

    suspend fun deleteNotification(id: Long): RemoteResponse<OKResponse, ErrorsListResponse>
}