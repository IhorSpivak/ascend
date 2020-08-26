package com.doneit.ascend.domain.entity.dto

class MessageDTO(
    val id: Long,
    val message: String,
    val userId: Long,
    val attachmentUrl: String,
    val attachmentType: String
)