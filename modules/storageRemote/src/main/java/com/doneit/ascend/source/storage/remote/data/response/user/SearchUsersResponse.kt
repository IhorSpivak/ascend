package com.doneit.ascend.source.storage.remote.data.response.user

import com.doneit.ascend.source.storage.remote.data.response.ImageResponse
import com.google.gson.annotations.SerializedName

data class SearchUsersResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("image") val image: ImageResponse?
)