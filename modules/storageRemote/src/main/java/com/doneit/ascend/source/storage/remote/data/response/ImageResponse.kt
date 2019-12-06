package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class ImageResponse(
    @SerializedName("url") val url: String?,
    @SerializedName("thumbnail") val thumbnail: ThumbnailResponse?
)