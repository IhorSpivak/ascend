package com.doneit.ascend.push.data

import com.google.gson.annotations.SerializedName

data class PushEvent(
    val title: String,
    @SerializedName("group_id") val groupId: Long?,
    @SerializedName("type")  val type: String
)