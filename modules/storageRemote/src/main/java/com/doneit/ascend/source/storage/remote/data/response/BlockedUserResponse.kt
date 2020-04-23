package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

class BlockedUserResponse (
    @SerializedName("id")
    val id: Long,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("image")
    val image: ImageResponse?
)