package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class RateResponse(
    @SerializedName("value") val value: Float,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("image") val image: ImageResponse,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)