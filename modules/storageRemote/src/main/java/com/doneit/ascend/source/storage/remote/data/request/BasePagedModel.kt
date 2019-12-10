package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

open class BasePagedModel(
    @SerializedName("page") val page: Int?,
    @SerializedName("per_page") val perPage: Int?,
    @SerializedName("sort_column") val sortColumn: String?,
    @SerializedName("sort_type") val sortType: String?
)