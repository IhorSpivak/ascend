package com.doneit.ascend.source.storage.remote.data.request.group

import com.google.gson.annotations.SerializedName

data class InviteToGroupRequest(
    @SerializedName("participants") val participants: List<String>
)