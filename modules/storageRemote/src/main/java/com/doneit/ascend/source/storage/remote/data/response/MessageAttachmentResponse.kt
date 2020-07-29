package com.doneit.ascend.source.storage.remote.data.response


import com.google.gson.annotations.SerializedName

data class MessageAttachmentResponse(
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("size")
    val size: String? = null,
    @SerializedName("type")
    val type: String? = null,
    @SerializedName("url")
    val url: String? = null
)