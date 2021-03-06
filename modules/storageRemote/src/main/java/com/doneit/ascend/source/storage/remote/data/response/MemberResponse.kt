package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class MemberResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("online")
    val online: Boolean,
    @SerializedName("leaved")
    val leaved: Boolean,
    @SerializedName("removed")
    val removed: Boolean,
    @SerializedName("image")
    val image: ImageResponse?
)