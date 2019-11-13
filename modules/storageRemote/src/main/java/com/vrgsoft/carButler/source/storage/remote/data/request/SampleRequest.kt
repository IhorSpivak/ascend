package com.vrgsoft.carButler.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

class SampleRequest(
    @SerializedName("social_type")
    val socialType: String,
    @SerializedName("social_token")
    val socialToken: String,
    @SerializedName("is_signup")
    val isSignup: Boolean
)