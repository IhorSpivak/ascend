package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class CreateGroupRequest(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("group_type") val groupType: String,
    @SerializedName("price") val price: String,
    @SerializedName("participants") val participants: List<String>?,
    @SerializedName("wdays") val days: List<Int>?,
    @SerializedName("meetings_count") val meetingsCount: Int
)