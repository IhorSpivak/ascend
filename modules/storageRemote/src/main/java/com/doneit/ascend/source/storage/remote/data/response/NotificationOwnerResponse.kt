package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class NotificationOwnerResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("display_name")
    val displayName: String
)