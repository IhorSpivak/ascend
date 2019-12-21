package com.doneit.ascend.source.storage.remote.repository.notification

import com.doneit.ascend.source.storage.remote.api.NotificationApi
import com.doneit.ascend.source.storage.remote.data.request.NotificationListRequest
import com.doneit.ascend.source.storage.remote.data.response.NotificationsListResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class NotificationRepository(
    gson: Gson,
    private val api: NotificationApi
) : BaseRepository(gson), INotificationRepository {

    override suspend fun getAllNotifications(request: NotificationListRequest): RemoteResponse<NotificationsListResponse, ErrorsListResponse> {
        return execute({ api.getNotificationsListAsync(
            request.page,
            request.perPage,
            request.sortColumn,
            request.sortType,
            request.notificationType,
            request.createdAtFrom,
            request.createdAtTo,
            request.updatedAtFrom,
            request.updatedAtTo
        ) }, ErrorsListResponse::class.java)
    }

    override suspend fun deleteNotification(id: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.deleteNotificationAsync(id) }, ErrorsListResponse::class.java)
    }
}