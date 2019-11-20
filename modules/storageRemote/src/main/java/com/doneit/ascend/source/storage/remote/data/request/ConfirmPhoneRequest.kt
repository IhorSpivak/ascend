package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class ConfirmPhoneRequest(
    @SerializedName("phone_number") val phoneNumber: String
)