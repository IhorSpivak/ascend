package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class CreateChatRequest(
    @SerializedName("title") val title: String,
    @SerializedName("chat_members") val chatMembers: List<Int>
)