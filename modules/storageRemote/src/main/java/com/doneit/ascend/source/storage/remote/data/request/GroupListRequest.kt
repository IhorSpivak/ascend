package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

//used like dto
class GroupListRequest(
    page: Int?,
    perPage: Int?,
    sortColumn: String?,
    sortType: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("user_id") val userId: Long?,
    @SerializedName("group_type") val groupType: String?
): BasePagedModel(page, perPage, sortColumn, sortType)