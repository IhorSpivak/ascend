package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class LeaveCommentRequest(
    @SerializedName("text")
    val comment: String
)