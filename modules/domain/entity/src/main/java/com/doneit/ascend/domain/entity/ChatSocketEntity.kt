package com.doneit.ascend.domain.entity

data class MessageSocketEntity(
    val id: Long,
    val message: String?,
    val status: String?,
    val edited: Boolean?,
    val type: String?,
    val userId: Long,
    val createdAt: String?,
    val updatedAt: String?
)
