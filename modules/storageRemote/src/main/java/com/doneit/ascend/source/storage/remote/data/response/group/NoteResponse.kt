package com.doneit.ascend.source.storage.remote.data.response.group

import com.google.gson.annotations.SerializedName

data class NoteResponse(
    @SerializedName("content") val content: String,
    @SerializedName("updated_at") val updatedAt: String
)