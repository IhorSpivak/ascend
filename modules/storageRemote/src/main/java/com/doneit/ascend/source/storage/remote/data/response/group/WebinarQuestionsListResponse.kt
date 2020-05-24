package com.doneit.ascend.source.storage.remote.data.response.group

import com.doneit.ascend.source.storage.remote.data.response.CommunityResponse
import com.doneit.ascend.source.storage.remote.data.response.QuestionResponse
import com.google.gson.annotations.SerializedName

data class WebinarQuestionsListResponse(
    @SerializedName("questions") val questions: List<WebinarQuestionResponse>
)