package com.doneit.ascend.source.storage.remote.data.response

import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.google.gson.annotations.SerializedName

class PurchasesResponse(
    @SerializedName("purchases") val purchases: List<PurchaseResponse>,
    count: Int
) : PagedResponse(count)