package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class PaymentSourceResponse(
    @SerializedName("payment_source_id") val sourceId: Long,
    @SerializedName("payment_source_type") val sourceType: String,
    @SerializedName("name") val name: String,
    @SerializedName("brand") val brand: String,
    @SerializedName("last4") val last4: String
)