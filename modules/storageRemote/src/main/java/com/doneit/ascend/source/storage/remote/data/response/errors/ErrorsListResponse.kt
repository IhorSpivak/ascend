package com.doneit.ascend.source.storage.remote.data.response.errors

import com.google.gson.annotations.SerializedName

data class ErrorsListResponse(
    @SerializedName("errors") val errors: List<String> = listOf()
)