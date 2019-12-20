package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class RatingRequest(
    @SerializedName("value") val value: Int
)