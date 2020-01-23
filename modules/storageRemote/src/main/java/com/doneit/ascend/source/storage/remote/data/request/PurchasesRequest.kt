package com.doneit.ascend.source.storage.remote.data.request

import com.doneit.ascend.source.storage.remote.data.request.base.BasePagedModel
import com.google.gson.annotations.SerializedName

class PurchasesRequest(
    page: Int?,
    perPage: Int?,
    sortColumn: String?,
    sortType: String?,
    @SerializedName("created_at_from") val createdAtFrom: String?,
    @SerializedName("created_at_to") val createdAtTo: String?
) : BasePagedModel(page, perPage, sortColumn, sortType)