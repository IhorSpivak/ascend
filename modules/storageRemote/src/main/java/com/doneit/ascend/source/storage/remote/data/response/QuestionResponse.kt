package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class QuestionResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("question_type") val type: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("answer_options") val options: List<AnswerOptionResponse>?
)