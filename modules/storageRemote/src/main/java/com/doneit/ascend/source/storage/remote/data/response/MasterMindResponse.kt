package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class MasterMindResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("description") val description: String,
    @SerializedName("location") val location: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("rating") val rating: Float,
    @SerializedName("groups_count") val groupsCount: Int,
    @SerializedName("followed") val followed: Boolean,
    @SerializedName("rated") val rated: Boolean,
    @SerializedName("image") val image: ImageResponse,
    @SerializedName("allow_rating") val allowRating: Boolean,
    @SerializedName("my_rating") val myRating: Int?
)