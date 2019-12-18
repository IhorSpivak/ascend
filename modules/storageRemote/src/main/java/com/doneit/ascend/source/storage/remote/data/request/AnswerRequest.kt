package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class AnswerRequest(
    @SerializedName("community") val community: String,
    @SerializedName("answers") val answers: List<AnswerItemRequest>
)