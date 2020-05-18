package com.doneit.ascend.source.storage.remote.data.response.group

import com.google.gson.annotations.SerializedName

data class GroupCredentialsResponse(
    @SerializedName("room_name") val name: String?,
    @SerializedName("access_token") val token: String?,
    @SerializedName("chat_id") val chatId: String?
)