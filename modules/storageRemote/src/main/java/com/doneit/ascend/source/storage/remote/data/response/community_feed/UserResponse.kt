package com.doneit.ascend.source.storage.remote.data.response.community_feed

import com.doneit.ascend.source.storage.remote.data.response.ImageResponse
import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("full_name")
    val fullName: String,
    val image: ImageResponse? = null)