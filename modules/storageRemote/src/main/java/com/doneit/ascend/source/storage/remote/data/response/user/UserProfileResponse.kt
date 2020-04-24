package com.doneit.ascend.source.storage.remote.data.response.user

import com.doneit.ascend.source.storage.remote.data.response.ImageResponse
import com.google.gson.annotations.SerializedName


data class UserProfileResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number") val phone: String,
    @SerializedName("location") val location: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("meeting_started") val meetingStarted: Boolean?,
    @SerializedName("new_groups") val newGroups: Boolean?,
    @SerializedName("invite_to_a_meeting") val inviteToMeeting: Boolean?,
    @SerializedName("social_type") val socialType: String?,
    @SerializedName("unanswered_questions") val unansweredQuestions: List<Int>?,
    @SerializedName("unread_notifications_count") val unreadNotificationsCount: Int?,
    @SerializedName("blocked_users_count") val blockedUsersCount: Int?,
    @SerializedName("visited_groups_count") val visitedGroupsCount: Int,
    @SerializedName("image") val image: ImageResponse?,
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("community") val community: String?
)