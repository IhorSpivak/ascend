package com.doneit.ascend.source.storage.remote.data.response.user

import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("current_user") val currrentUser: UserProfileResponse
)