package com.doneit.ascend.source.storage.remote.data.response.base

import com.google.gson.annotations.SerializedName

open class PagedResponse (
    @SerializedName("count") val count: Int? = null
)