package com.doneit.ascend.source.storage.remote.data.request.community_feed

import com.doneit.ascend.source.storage.remote.data.request.base.BasePagedModel
import com.google.gson.annotations.SerializedName

class CommentsRequest(
    page: Int?,
    perPage: Int?,
    sortColumn: String?,
    sortType: String?,
    @SerializedName("created_at_from")
    val createdAtFrom: String?,
    @SerializedName("created_at_to")
    val createdAtTo: String?,
    @SerializedName("updated_at_from")
    val updatedAtFrom: String?,
    @SerializedName("updated_at_to")
    val updatedAtTo: String?,
    val text: String?
) : BasePagedModel(page, perPage, sortColumn, sortType)