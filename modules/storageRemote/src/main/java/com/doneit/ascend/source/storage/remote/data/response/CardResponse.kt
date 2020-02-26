package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class CardResponse(
    @SerializedName("id")val id: Long,
    @SerializedName("fullName") val name: String,
    @SerializedName("brand")val brand: String,
    @SerializedName("country")val country: String,
    @SerializedName("exp_month")val expMonth: Int,
    @SerializedName("exp_year")val expYear: Int,
    @SerializedName("last4")val last4: String,
    @SerializedName("created_at")val cratedAt: String,
    @SerializedName("updated_at")val updatedAt: String,
    @SerializedName("default")val isDefault: Boolean
)