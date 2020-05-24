package com.doneit.ascend.source.storage.remote.data.request

import com.doneit.ascend.source.storage.remote.data.request.base.BasePagedModel
import com.google.gson.annotations.SerializedName

class WebinarQuestionsListRequest(
    page: Int?,
    perPage: Int?,
    sortColumn: String?,
    sortType: String?,
    val content: String?,
    @SerializedName("user_id") val userId: Long?,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("created_at_from")
    val createdAtFrom: String?,
    @SerializedName("created_at_to")
    val createdAtTo: String?,
    @SerializedName("updated_at_from")
    val updatedAtFrom: String?,
    @SerializedName("updated_at_to")
    val updatedAtTo: String?
) : BasePagedModel(page, perPage, sortColumn, sortType)