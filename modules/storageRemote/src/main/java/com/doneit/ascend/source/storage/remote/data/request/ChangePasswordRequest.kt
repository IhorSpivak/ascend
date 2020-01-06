package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("current_password") val currentPassword: String,
    @SerializedName("password") val password: String,
    @SerializedName("password_confirmation") val passwordConfirmation: String
)