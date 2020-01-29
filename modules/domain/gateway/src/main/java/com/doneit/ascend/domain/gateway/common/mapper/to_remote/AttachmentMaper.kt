package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.AttachmentsListModel
import com.doneit.ascend.source.storage.remote.data.request.AttachmentListRequest

fun AttachmentsListModel.toRequest(page: Int): AttachmentListRequest {
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