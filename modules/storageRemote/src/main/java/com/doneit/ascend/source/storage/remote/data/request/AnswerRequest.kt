package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class AnswerRequest(
    @SerializedName("question_id") val questionId: Long,
    @SerializedName("answer") val answer: String?,
    @SerializedName("answer_option_id") val answerOptionId: Long?
)