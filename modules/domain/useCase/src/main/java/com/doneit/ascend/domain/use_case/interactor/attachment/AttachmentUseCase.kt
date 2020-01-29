package com.doneit.ascend.domain.use_case.interactor.attachment

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.dto.AttachmentsListModel

interface AttachmentUseCase {
    suspend fun getAttachmentListPaged(model: AttachmentsListModel): PagedList<AttachmentEntity>
}