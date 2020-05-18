package com.doneit.ascend.source.storage.remote.data.request.group

import com.doneit.ascend.source.storage.remote.data.request.base.BasePagedModel
import com.google.gson.annotations.SerializedName

//used like dto
class GroupListRequest(
    page: Int?,
    perPage: Int?,
    sortColumn: String?,
    sortType: String?,
    @SerializedName("full_name") val name: String?,
    @SerializedName("user_id") val userId: Long?,
    @SerializedName("group_type") val groupType: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("my_groups") val myGroups: Boolean?,
    @SerializedName("start_time_from") val startTimeFrom: String?,
    @SerializedName("start_time_to") val startTimeTo: String?,
    @SerializedName("community") val community: String?,
    @SerializedName("tag_id") val tagId: Int?
): BasePagedModel(page, perPage, sortColumn, sortType)