package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("message")
    val message: String,
    @SerializedName("edited")
    val edited: Boolean?,
    @SerializedName("user_id")
    val userId: Long,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("status")
    val status: String
)