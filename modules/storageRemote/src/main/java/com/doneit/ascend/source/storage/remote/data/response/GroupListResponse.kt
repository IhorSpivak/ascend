package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class GroupListResponse(
    @SerializedName("groups") val groups: List<GroupResponse>
)