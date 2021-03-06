package com.doneit.ascend.source.storage.remote.data.response

import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.google.gson.annotations.SerializedName

class RatesResponse(
    count: Int,
    @SerializedName("ratings") val ratings: List<RateResponse>
): PagedResponse(count)