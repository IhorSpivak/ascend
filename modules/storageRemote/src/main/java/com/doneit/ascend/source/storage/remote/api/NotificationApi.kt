package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.response.NotificationsListResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {

    @GET("notifications")
    fun getNotificationsListAsync(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("sort_column") sortColumn: String?,
        @Query("sort_type") sortType: String?,
        @Query("notification_type") notificationType: String?,
        @Query("created_at_from") createdAtFrom: String?,
        @Query("created_at_to") createdAtTo: String?,
        @Query("updated_at_from") updatedAtFrom: String?,
        @Query("updated_at_to") updatedAtTo: String?
    ): Deferred<Response<NotificationsListResponse>>

    @DELETE("notifications/{id}")
    fun deleteNotificationAsync(@Path("id") id: Long): Deferred<Response<OKResponse>>
}