package com.doneit.ascend.domain.entity

data class PageEntity(
    val pageType: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val errors: List<String>?
)