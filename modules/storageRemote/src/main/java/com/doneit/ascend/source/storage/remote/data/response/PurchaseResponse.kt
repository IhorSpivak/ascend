package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class PurchaseResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("amount") val amount: Float,
    @SerializedName("group_id") val groupId: Long,
    @SerializedName("group_name") val groupName: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("payment_source") val paymentSource: PaymentSourceResponse
)