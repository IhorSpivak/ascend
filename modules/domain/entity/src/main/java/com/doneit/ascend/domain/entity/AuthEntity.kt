package com.doneit.ascend.domain.entity

data class AuthEntity(
    val token: String,
    val userEntity: UserEntity
)