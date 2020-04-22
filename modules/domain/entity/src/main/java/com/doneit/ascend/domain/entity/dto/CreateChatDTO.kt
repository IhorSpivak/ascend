package com.doneit.ascend.domain.entity.dto

data class CreateChatDTO(
    val title: String? = null,
    val members: List<Int>? = null
)