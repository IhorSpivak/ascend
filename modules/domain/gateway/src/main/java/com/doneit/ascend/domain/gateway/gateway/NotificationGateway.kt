package com.doneit.ascend.domain.gateway.gateway

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.NotificationListModel
import com.doneit.ascend.domain.entity.notification.NotificationEntity
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.boundaries.NotificationBoundaryCallback
import com.doneit.ascend.domain.use_case.gateway.INotificationGateway
import com.doneit.ascend.source.storage.remote.repository.notification.INotificationRepository
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

internal class NotificationGateway(
    errors: NetworkManager,
    private val local: com.doneit.ascend.source.storage.local.repository.notification.INotificationRepository,
    private val remote: INotificationRepository
) : BaseGateway(errors), INotificationGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override fun getNotificationList(request: NotificationListModel) =
        liveData<PagedList<NotificationEntity>> {
            local.removeAll()

            val config = PagedList.Config.Builder()
                .setEnablePlaceholders(false)
                .setPageSize(request.perPage ?: 10)
                .build()
            val factory = local.getList().map { it.toEntity() }

            val boundary = NotificationBoundaryCallback(
                GlobalScope,
                local,
                remote,
                request
            )

            emitSource(
                LivePagedListBuilder<Int, NotificationEntity>(factory, config)
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .setBoundaryCallback(boundary)
                    .build()
            )

            boundary.loadInitial()
        }

    override suspend fun delete(id: Long): ResponseEntity<Unit, List<String>> {
        val result = executeRemote { remote.deleteNotification(id) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )

        if(result.isSuccessful) {
            local.remove(id)
        }

        return result
    }

    override fun getUnreadLive(): LiveData<List<NotificationEntity>> {
        return local.getListLive().map { it.map { it.toEntity() } }
    }

    override fun notificationReceived(notification: NotificationEntity) {
        GlobalScope.launch(Dispatchers.IO) {
            local.insertAll(listOf(notification.toLocal()))
        }
    }
}