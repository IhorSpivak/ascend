package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class ThumbnailResponse(
    @SerializedName("url") val url: String
)