package com.doneit.ascend.domain.gateway.gateway.data_source

import androidx.paging.PageKeyedDataSource
import com.doneit.ascend.domain.entity.NotificationEntity
import com.doneit.ascend.domain.entity.NotificationOwnerEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.NotificationListModel
import com.doneit.ascend.domain.gateway.common.mapper.toResponseEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.remote.repository.notification.INotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.ceil

class NotificationDataSource(
    private val scope: CoroutineScope,
    private val remote: INotificationRepository,
    private val notificationRequest: NotificationListModel,
    private val items: List<NotificationEntity>
) : PageKeyedDataSource<Int, NotificationEntity>() {

    private var masterMindCount: Int? = null
    private var lastMMPage: Int? = null

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, NotificationEntity>
    ) {
        scope.launch {
            try {

                val page = 1

                val result = loadMasterMindList(page)

                if (masterMindCount != null) {
                    val perPage = notificationRequest.perPage ?: 10
                    lastMMPage = ceil(masterMindCount!!.toDouble() / perPage).toInt()
                } else {
                    lastMMPage = 0
                }

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
                masterMindCount = it?.count
                it?.notifications?.map { notificationIt -> notificationIt.toEntity() }
            },
            {
                it?.errors
            }
        )
    }
}