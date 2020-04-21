package com.doneit.ascend.source.storage.remote.data.request

import com.doneit.ascend.source.storage.remote.data.request.base.BasePagedModel
import com.google.gson.annotations.SerializedName

class MessageListRequest(
    page: Int?,
    perPage: Int?,
    sortColumn: String?,
    sortType: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("user_id")
    val userId: String?,
    @SerializedName("edited")
    val edited: Int?,
    @SerializedName("created_at_from")
    val createdAtFrom: String?,
    @SerializedName("created_at_to")
    val createdAtTo: String?,
    @SerializedName("updated_at_from")
    val updatedAtFrom: String?,
    @SerializedName("updated_at_to")
    val updatedAtTo: String?
) : BasePagedModel(page, perPage, sortColumn, sortType)