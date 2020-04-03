package com.doneit.ascend.source.storage.remote.data.request.group

import com.google.gson.annotations.SerializedName

data class CreateGroupRequest(
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("start_time") val startTime: String?,
    @SerializedName("group_type") val groupType: String?,
    @SerializedName("price") val price: Int?,
    @SerializedName("participants") val participants: List<String>?,
    @SerializedName("wdays") val days: List<Int>?,
    @SerializedName("meetings_count") val meetingsCount: Int?,
    @SerializedName("meeting_format") val meetingFormat: String?,
    @SerializedName("private") val private: Boolean?,
    @SerializedName("tag_id") val tagId: Int?
)