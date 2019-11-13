package com.vrgsoft.carButler.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("full_name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number") val phone: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)