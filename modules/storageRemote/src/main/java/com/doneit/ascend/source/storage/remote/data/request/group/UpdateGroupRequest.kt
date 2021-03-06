package com.doneit.ascend.source.storage.remote.data.request.group

import com.google.gson.annotations.SerializedName

data class UpdateGroupRequest(
    @SerializedName("name") val name: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("start_time") val startTime: String?,
    @SerializedName("group_type") val groupType: String?,
    @SerializedName("price") val price: Int?,
    @SerializedName("participants") val participants: List<String>?,
    @SerializedName("removed_participants") val removedParticipants: List<String>?,
    @SerializedName("wdays") val days: List<Int>?,
    @SerializedName("meetings_count") val meetingsCount: Int?,
    @SerializedName("meeting_format") val meetingFormat: String?,
    @SerializedName("private") val private: Boolean?,
    @SerializedName("tag_id") val tagId: Int?,
    @SerializedName("times") val times: List<String>?,
    @SerializedName("themes") val themes: List<String>?,
    @SerializedName("duration") val duration: Int?
)