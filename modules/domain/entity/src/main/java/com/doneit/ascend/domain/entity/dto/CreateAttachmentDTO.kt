package com.doneit.ascend.domain.entity.dto

import com.doneit.ascend.domain.entity.AttachmentType

data class CreateAttachmentDTO(
    val groupId: Long,
    val fileName: String? = null,
    val fileSize: Long? = null,
    val link: String,
    val attachmentType: AttachmentType,
    val private: Boolean
)