package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class CreateLiveEventRequest(
    @SerializedName("title") val title: String,
    @SerializedName("stream_title") val streamTitle: String
)