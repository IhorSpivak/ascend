package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("email")var email: String,
    @SerializedName("phone_number") var phone: String,
    @SerializedName("full_name") var name: String,
    @SerializedName("password") var password: String,
    @SerializedName("password_confirmation") var passwordConfirmation: String,
    @SerializedName("code") var code: String
)