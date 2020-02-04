package com.doneit.ascend.domain.entity

import java.util.*

data class AttachmentEntity(
    val id: Long,
    val fileName: String,
    val fileSize: Long,
    val link: String,
    val groupId: Long,
    val userId: Long,
    val private: Boolean,
    val attachmentType: AttachmentType,
    val createdAt: Date,
    val updatedAt: Date
)

enum class AttachmentType {
    FILE,
    IMAGE,
    LINK
}