package com.doneit.ascend.domain.entity.dto

data class CreateChatDTO(
    val title: String,
    val members: List<Int>
)