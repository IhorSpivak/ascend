package com.doneit.ascend.source.storage.remote.data.response.group

import com.doneit.ascend.source.storage.remote.data.response.ImageResponse
import com.google.gson.annotations.SerializedName

data class QuestionSocketEventResponse(
    @SerializedName("identifier") val identifier: String,
    @SerializedName("message") val message: QuestionSocketEventMessage?
)

data class QuestionSocketEventMessage(

    @SerializedName("id") val id: Long,
    @SerializedName("content") val question: String,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("image") val image: ImageResponse?,
    @SerializedName("event") val event: String?

)