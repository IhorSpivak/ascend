package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class CommunityResponse(
    @SerializedName("title") val title: String,
    @SerializedName("answer_options") val answerOptions: List<String>
)