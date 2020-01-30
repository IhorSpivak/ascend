package com.doneit.ascend.source.storage.remote.data.request.group

import com.doneit.ascend.source.storage.remote.data.request.base.BasePagedModel
import com.google.gson.annotations.SerializedName

//used like dto
class GroupParticipantsRequest(
    page: Int?,
    perPage: Int?,
    sortColumn: String?,
    sortType: String?,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("connected") val connected: Boolean?
): BasePagedModel(page, perPage, sortColumn, sortType)