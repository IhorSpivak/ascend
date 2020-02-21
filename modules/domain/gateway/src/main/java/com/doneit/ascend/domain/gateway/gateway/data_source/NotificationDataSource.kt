package com.doneit.ascend.domain.gateway.gateway.data_source

import androidx.paging.PageKeyedDataSource
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.NotificationListDTO
import com.doneit.ascend.domain.entity.notification.NotificationEntity
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.remote.repository.notification.INotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class NotificationDataSource(
    private val scope: CoroutineScope,
    private val remote: INotificationRepository,
    private val notificationRequest: NotificationListDTO,
    private val items: List<NotificationEntity>
) : PageKeyedDataSource<Int, NotificationEntity>() {

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, NotificationEntity>
    ) {
        scope.launch {
            try {

                val page = 1

                val result = loadMasterMindList(page)

                if (result.isSuccessful) {
                    callback.onResult(result.successModel ?: listOf(), null, page + 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadAfter(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, NotificationEntity>
    ) {
        scope.launch {
            try {

                val page = params.key

                val result = loadMasterMindList(page)

                if (result.isSuccessful) {
                    callback.onResult(result.successModel ?: listOf(), page + 1)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun loadBefore(
        params: LoadParams<Int>,
        callback: LoadCallback<Int, NotificationEntity>
    ) {
    }

    private suspend fun loadMasterMindList(page: Int): ResponseEntity<List<NotificationEntity>, List<String>> {

        val request = notificationRequest.toRequest(page)


        return remote.getAllNotifications(request).toResponseEntity(
            {
                it?.notifications?.map { notificationIt -> notificationIt.toEntity() }
            },
            {
                it?.errors
            }
        )
    }
}