package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.AttachmentsListDTO
import com.doneit.ascend.domain.entity.dto.CreateAttachmentDTO
import com.doneit.ascend.source.storage.remote.data.request.attachment.AttachmentListRequest
import com.doneit.ascend.source.storage.remote.data.request.attachment.CreateAttachmentRequest

fun AttachmentsListDTO.toRequest(page: Int): AttachmentListRequest {
    return AttachmentListRequest(
        page,
        perPage,
        sortColumn,
        sortType?.toString(),
        groupId,
        userId,
        fileName,
        link,
        private,
        createdAtFrom?.toRemoteString(),
        createdAtTo?.toRemoteString(),
        updatedAtFrom?.toRemoteString(),
        updatedAtTo?.toRemoteString()
    )
}

fun CreateAttachmentDTO.toRequest(): CreateAttachmentRequest {
    return CreateAttachmentRequest(
        groupId,
        fileName,
        fileSize,
        link,
        attachmentType.toString(),
        private
    )
}