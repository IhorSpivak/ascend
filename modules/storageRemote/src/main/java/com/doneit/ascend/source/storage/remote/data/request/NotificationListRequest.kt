package com.doneit.ascend.source.storage.remote.data.request

import com.doneit.ascend.source.storage.remote.data.request.base.BasePagedModel
import com.google.gson.annotations.SerializedName

class NotificationListRequest(
    page: Int?,
    perPage: Int?,
    sortColumn: String?,
    sortType: String?,
    @SerializedName("notification_type")
    val notificationType: String?,
    @SerializedName("created_at_from")
    val createdAtFrom: String?,
    @SerializedName("created_at_to")
    val createdAtTo: String?,
    @SerializedName("updated_at_from")
    val updatedAtFrom: String?,
    @SerializedName("updated_at_to")
    val updatedAtTo: String?
) : BasePagedModel(page, perPage, sortColumn, sortType)