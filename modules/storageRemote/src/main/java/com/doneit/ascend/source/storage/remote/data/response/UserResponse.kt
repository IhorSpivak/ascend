package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("full_name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number") val phone: String,
    @SerializedName("location") val location: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("meeting_started") val meetingStarted: Boolean?,
    @SerializedName("new_groups") val newGroups: Boolean?,
    @SerializedName("invite_to_a_meeting") val inviteToMeeting: Boolean?,
    @SerializedName("unanswered_questions") val unansweredQuestions: List<Int>?,
    @SerializedName("image") val image: ImageResponse?,
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("rating") val rating: Float,
    @SerializedName("role") val role: String?,
    @SerializedName("community") val community: String?
)