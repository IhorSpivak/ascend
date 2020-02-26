package com.doneit.ascend.domain.entity.user

import com.doneit.ascend.domain.entity.user.UserEntity

data class AuthEntity(
    val token: String,
    val userEntity: UserEntity
)