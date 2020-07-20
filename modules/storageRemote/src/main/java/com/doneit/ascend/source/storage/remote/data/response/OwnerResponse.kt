package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class OwnerResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("image") val image: ImageResponse?,
    @SerializedName("rating") val rating: Float,
    @SerializedName("followed") val followed: Boolean,
    @SerializedName("location") val location: String?,
    @SerializedName("connected") val connected: Boolean?
)