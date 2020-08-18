package com.doneit.ascend.source.storage.remote.data.response.group

import com.google.gson.annotations.SerializedName

data class BannerResponse(
    @SerializedName("title") val title: String? = null,
    @SerializedName("banner_type") val bannerType: String? = null
)