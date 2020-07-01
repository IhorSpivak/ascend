package com.doneit.ascend.source.storage.remote.data.response.group

import com.google.gson.annotations.SerializedName

data class WebinarCredentialsResponse(
    @SerializedName("key") val key: String?,
    @SerializedName("link") val link: String?,
    @SerializedName("chat_id") val chatId: Long
)