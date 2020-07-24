package com.doneit.ascend.source.storage.remote.data.response.community_feed


import com.google.gson.annotations.SerializedName

data class AttachmentResponse(
    @SerializedName("content_type")
    val contentType: String? = null,
    @SerializedName("id")
    val id: Long,
    @SerializedName("url")
    val url: String? = null,
    @SerializedName("size")
    val size: SizeResponse,
    @SerializedName("thumbnail")
    val thumbnail: String? = null
)

data class SizeResponse(
    val width: Int? = null,
    val height: Int? = null
)