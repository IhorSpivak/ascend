package com.doneit.ascend.source.storage.remote.data.request

import com.doneit.ascend.source.storage.remote.data.request.base.BasePagedModel
import com.google.gson.annotations.SerializedName

class BlockedUsersRequest(
    page: Int?,
    perPage: Int?,
    sortColumn: String?,
    sortType: String?,
    @SerializedName("full_name")
    val fullName: String?
) : BasePagedModel(page, perPage, sortColumn, sortType)