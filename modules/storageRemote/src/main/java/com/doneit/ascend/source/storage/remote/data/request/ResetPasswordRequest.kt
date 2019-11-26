package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("phone_number") var phone: String,
    @SerializedName("code") var code: String,
    @SerializedName("password") var password: String,
    @SerializedName("password_confirmation") var passwordConfirmation: String
)