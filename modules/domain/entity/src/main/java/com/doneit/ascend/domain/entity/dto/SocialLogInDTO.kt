package com.doneit.ascend.domain.entity.dto

data class SocialLogInDTO(
    val socialType: String,
    val accessToken: String,
    val accessTokenSecret: String?
)