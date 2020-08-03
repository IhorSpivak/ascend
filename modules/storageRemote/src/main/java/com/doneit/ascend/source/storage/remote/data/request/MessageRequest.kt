package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class MessageRequest(
    val id: Long,
    @SerializedName("message") val message: String,
    val attachment: AttachmentRequest?
)