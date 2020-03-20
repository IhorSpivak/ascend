package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class TagResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val tag: String
)