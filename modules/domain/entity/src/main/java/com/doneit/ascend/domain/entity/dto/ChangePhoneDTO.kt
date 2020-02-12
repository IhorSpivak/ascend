package com.doneit.ascend.domain.entity.dto

data class ChangePhoneDTO(
    val password: String,
    val phoneNumber: String,
    val code: String
)