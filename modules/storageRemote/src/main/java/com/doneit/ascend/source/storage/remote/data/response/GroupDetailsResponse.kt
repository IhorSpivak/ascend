package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class GroupDetailsResponse (
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("start_time") val startTime: String,
    @SerializedName("group_type") val groupType: String,
    @SerializedName("price") val price: Float,
    @SerializedName("image") val image: ImageResponse,
    @SerializedName("meetings_count") val meetingsCount: Int,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("owner") val owner: OwnerResponse,
    @SerializedName("subscribed") val subscribed: Boolean,
    @SerializedName("invited") val invited: Boolean,
    @SerializedName("participants_count") val participantsCount: Int,
    @SerializedName("invites_count") val invitesCount: Int
)