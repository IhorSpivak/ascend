package com.doneit.ascend.source.storage.remote.data.response.community_feed


import com.google.gson.annotations.SerializedName

data class AttachmentResponse(
    @SerializedName("content_type")
    val contentType: String? = null,
    @SerializedName("id")
    val id: String? = null,
    @SerializedName("url")
    val url: String? = null
)