package com.doneit.ascend.domain.gateway.gateway

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.NotificationEntity
import com.doneit.ascend.domain.entity.NotificationOwnerEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.NotificationListModel
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toDate
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.gateway.gateway.data_source.NotificationDataSource
import com.doneit.ascend.domain.use_case.gateway.INotificationGateway
import com.doneit.ascend.source.storage.remote.repository.notification.INotificationRepository
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.GlobalScope

internal class NotificationGateway(
    errors: NetworkManager,
    private val remote: INotificationRepository
) : BaseGateway(errors), INotificationGateway {

    //todo remove mock data
    private val items = mutableListOf(
        NotificationEntity(
            1,
            "invite_to_a_meeting",
            16,
            "Group",
            "2019-12-05T15:32:20.053Z".toDate(),
            "2019-12-05T15:32:20.053Z".toDate(),
            NotificationOwnerEntity(
                1,
                "Gregory House",
                "Dr. G. House"
            )
        ),
        NotificationEntity(
            2,
            "new_groups",
            17,
            "Group",
            "2019-12-05T15:32:20.053Z".toDate(),
            "2019-12-05T15:32:20.053Z".toDate(),
            NotificationOwnerEntity(
                1,
                "Gregory House",
                "Dr. G. House"
            )
        ),
        NotificationEntity(
            3,
            "meeting_started",
            18,
            "Group",
            "2019-12-05T15:32:20.053Z".toDate(),
            "2019-12-05T15:32:20.053Z".toDate(),
            NotificationOwnerEntity(
                1,
                "Gregory House",
                "Dr. G. House"
            )
        )
    )

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun getNotificationList(request: NotificationListModel): PagedList<NotificationEntity> {

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(request.perPage ?: 10)
            .build()

        val dataSource = NotificationDataSource(
            GlobalScope,
            remote,
            request,
            items
        )

        val executor = MainThreadExecutor()

        return PagedList.Builder<Int, NotificationEntity>(dataSource, config)
            .setFetchExecutor(executor)
            .setNotifyExecutor(executor)
            .build()
    }

    override suspend fun delete(id: Long): ResponseEntity<Unit, List<String>> {

        val item = items.find { it -> it.id == id }

        if (item != null) {
            items.remove(item)
        }

        return executeRemote { remote.deleteNotification(id) }.toResponseEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }
}