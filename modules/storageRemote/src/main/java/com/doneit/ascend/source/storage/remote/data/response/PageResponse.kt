package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class PageResponse(
    @SerializedName("page_type") val pageType: String,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("errors") val errors: List<String>?
)