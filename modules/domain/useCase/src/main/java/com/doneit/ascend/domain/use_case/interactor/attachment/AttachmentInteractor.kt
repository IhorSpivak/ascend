package com.doneit.ascend.domain.use_case.interactor.attachment

import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.dto.AttachmentsListModel
import com.doneit.ascend.domain.use_case.gateway.IAttachmentGateway

internal class AttachmentInteractor(
    private val groupGateway: IAttachmentGateway
) : AttachmentUseCase {
    override suspend fun getAttachmentListPaged(model: AttachmentsListModel): PagedList<AttachmentEntity> {
        return groupGateway.getAttachments(model)
    }
}