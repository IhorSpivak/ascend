package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class SubscribeGroupRequest(
    @SerializedName("payment_source_id") val paymentSourceId: Long?,
    @SerializedName("payment_source_type") val paymentSourceType: String?
)