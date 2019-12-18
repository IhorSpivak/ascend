package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class QuestionsListResponse(
    @SerializedName("questions") val questions: List<QuestionResponse>,
    @SerializedName("community") val community: CommunityResponse
)