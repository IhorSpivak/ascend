package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("full_name") val fullName: String?,
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("meeting_started") val isMeetingStarted: Boolean?,
    @SerializedName("new_groups") val hasNewGroups: Boolean?,
    @SerializedName("invite_to_a_meeting") val hasInviteToMeeting: Boolean?,
    @SerializedName("age") val age: Int?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("description") val description: String?
)