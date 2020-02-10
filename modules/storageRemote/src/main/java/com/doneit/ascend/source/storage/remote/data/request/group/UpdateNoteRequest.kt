package com.doneit.ascend.source.storage.remote.data.request.group

import com.google.gson.annotations.SerializedName

data class UpdateNoteRequest(
    @SerializedName("content") val content: String
)