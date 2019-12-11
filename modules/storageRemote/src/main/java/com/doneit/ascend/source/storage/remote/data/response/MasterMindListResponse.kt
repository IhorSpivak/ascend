package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class MasterMindListResponse(
    @SerializedName("users") val users: List<MasterMindResponse>
)