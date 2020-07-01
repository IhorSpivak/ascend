package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class ActivateLiveEventResponse(
    val live: LiveResponse
)

data class LiveResponse(
    @SerializedName("key")
    val streamKey: String
)