package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("notification_type")
    val notificationType: String,
    @SerializedName("group_id")
    val groupId: Long,
    @SerializedName("group_name")
    val groupName: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("owner")
    val owner: NotificationOwnerResponse
)