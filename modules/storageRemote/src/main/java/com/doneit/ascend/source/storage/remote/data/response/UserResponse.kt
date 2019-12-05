package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("full_name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone_number") val phone: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("unanswered_questions") val unansweredQuestions: List<Int>?,
    @SerializedName("rating") val rating: Int?,
    @SerializedName("role") val role: String?,
    @SerializedName("community") val community: String?

)