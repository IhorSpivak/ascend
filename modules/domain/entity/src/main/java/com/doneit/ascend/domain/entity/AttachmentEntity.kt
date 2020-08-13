package com.doneit.ascend.domain.entity

import com.doneit.ascend.domain.entity.community_feed.ContentType
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

    //TODO: image type don't exist on server (used FILE instead)
    VIDEO,
    IMAGE,
    LINK,
    UNEXPECTED;

    override fun toString(): String {
        return super.toString().toLowerCase()
    }

    fun toContentType(): ContentType? {
        return when (this) {
            VIDEO -> ContentType.VIDEO
            IMAGE -> ContentType.IMAGE
            else -> null
        }
    }

    companion object {
        fun fromRemoteString(representation: String): AttachmentType {
            return values().firstOrNull { it.toString().toLowerCase() == representation }
                ?: UNEXPECTED
        }
    }
}