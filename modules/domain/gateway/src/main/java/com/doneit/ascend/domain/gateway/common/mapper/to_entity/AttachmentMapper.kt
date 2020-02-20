package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.AttachmentType
import com.doneit.ascend.source.storage.local.data.AttachmentLocal
import com.doneit.ascend.source.storage.remote.data.response.AttachmentResponse

fun AttachmentResponse.toEntity(): AttachmentEntity {
    return AttachmentEntity(
        id,
        fileName,
        fileSize,
        link,
        groupId,
        userId,
        private,
        attachmentType.toAttachmentType(),
        createdAt.toDate()!!,
        updatedAt.toDate()!!
    )
}

fun AttachmentLocal.toEntity(): AttachmentEntity {
    var attachmentType = AttachmentType.fromRemoteString(attachmentType)
    //TODO: workaround for image holder, don't use for now
    /*IMAGE_EXTENSIONS.forEach {
        if(fileName.endsWith(it)) {
            attachmentType = AttachmentType.IMAGE
        }
    }*/

    return AttachmentEntity(
        id,
        fileName,
        fileSize,
        link,
        groupId,
        userId,
        privacy,
        attachmentType,
        createdAt.toDate()!!,
        updatedAt.toDate()!!
    )
}

fun String.toAttachmentType(): AttachmentType {
    return AttachmentType.valueOf(this.toUpperCase())
}