package com.doneit.ascend.source.storage.remote.data.response.group

import com.doneit.ascend.source.storage.remote.data.response.ImageResponse
import com.google.gson.annotations.SerializedName

data class WebinarQuestionResponse(
    @SerializedName("key") val id: Long?,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("user_name") val userName: String,
    @SerializedName("image") val imageResponse: ImageResponse
)
