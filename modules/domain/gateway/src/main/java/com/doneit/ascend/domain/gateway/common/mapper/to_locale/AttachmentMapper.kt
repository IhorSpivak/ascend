package com.doneit.ascend.domain.gateway.common.mapper.to_locale

import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRemoteString
import com.doneit.ascend.source.storage.local.data.AttachmentLocal

fun AttachmentEntity.toLocal(): AttachmentLocal {
    return AttachmentLocal(
        id,
        fileName,
        fileSize,
        link,
        groupId,
        userId,
        private,
        attachmentType.toString(),
        createdAt.toRemoteString(),
        updatedAt.toRemoteString()
    )
}