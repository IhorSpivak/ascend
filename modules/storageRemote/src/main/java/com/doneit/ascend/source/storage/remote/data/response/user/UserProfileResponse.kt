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
    @SerializedName("description") val description: String?,
    @SerializedName("bio") val bio: String?,
    @SerializedName("birthday") val birthday: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("rated") val rated: Boolean?,
    @SerializedName("followed") val followed: Boolean?,
    @SerializedName("allow_rating") val allowRating: Boolean?,
    @SerializedName("rating") val rating: Float?,
    @SerializedName("groups_count") val groupsCount: Int?,
    @SerializedName("followers_count") val followersCount: Int?,
    @SerializedName("my_rating") val myRating: Int?,
    @SerializedName("community") val community: String?,
    @SerializedName("created_channels_count") val created_channels_count: Int?,
    @SerializedName("communities") val communities: List<String>?,
    @SerializedName("have_subscription") val haveSubscription: Boolean?,
    @SerializedName("subscription_trial") val subscriptionTrial: Boolean?,
    @SerializedName("subscription_cancelled") val subscriptionCanceled: Boolean?,
    @SerializedName("stripe_fields_needed") val stripeFieldsNeeded: List<String>?,
    @SerializedName("stripe_required_fields_filled") val stripeRequiredFieldsFilled: Boolean?
)