package com.doneit.ascend.domain.gateway.gateway.boundaries

import com.doneit.ascend.domain.entity.dto.NotificationListDTO
import com.doneit.ascend.domain.entity.notification.NotificationEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.local.repository.notification.INotificationRepository
import com.vrgsoft.core.gateway.orZero
import kotlinx.coroutines.CoroutineScope

class NotificationBoundaryCallback(
    scope: CoroutineScope,
    private val local: INotificationRepository,
    private val remote: com.doneit.ascend.source.storage.remote.repository.notification.INotificationRepository,
    private val notificationListModel: NotificationListDTO
) : BaseBoundary<NotificationEntity>(scope) {


    override suspend fun fetchPage() {
        val response = remote.getAllNotifications(notificationListModel.toRequest(pageIndexToLoad))
        if (response.isSuccessful) {
            val model = response.successModel!!.notifications?.map { it.toEntity() }

            model?.let {
                val loadedCount = model.size
                val remoteCount = response.successModel!!.count

                receivedItems(loadedCount, remoteCount.orZero())

                local.insertAll(model.map { it.toLocal() })
            }
        }
    }
}