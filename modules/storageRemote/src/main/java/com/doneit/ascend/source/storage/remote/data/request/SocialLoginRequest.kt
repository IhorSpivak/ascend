package com.doneit.ascend.source.storage.remote.data.request

import com.google.gson.annotations.SerializedName

data class SocialLoginRequest(
    @SerializedName("social_type") val socialType: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("access_token_secret") val accessTokenSecret: String?,
    @SerializedName("firebase_id") val firebaseId: String?
)