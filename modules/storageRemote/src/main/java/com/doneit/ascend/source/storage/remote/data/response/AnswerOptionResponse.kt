package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class AnswerOptionResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("position") val position: Long,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)