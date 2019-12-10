package com.doneit.ascend.domain.entity.dto

data class SocialLogInModel(
    val socialType: String,
    val accessToken: String,
    val accessTokenSecret: String?
)