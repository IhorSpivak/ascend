package com.doneit.ascend.domain.use_case.gateway

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.dto.AttachmentsListModel

interface IAttachmentGateway {
    suspend fun getAttachments(listModel: AttachmentsListModel): PagedList<AttachmentEntity>
}