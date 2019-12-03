package com.doneit.ascend.domain.entity

data class SocialLogInModel(
    val socialType: String,
    val accessToken: String,
    val accessTokenSecret: String?
)