package com.doneit.ascend.source.storage.remote.data.request.group

import com.google.gson.annotations.SerializedName

data class CancelGroupRequest (
    @SerializedName("reason") val reason: String
)