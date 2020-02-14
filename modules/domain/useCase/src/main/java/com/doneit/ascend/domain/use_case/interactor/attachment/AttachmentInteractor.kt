package com.doneit.ascend.domain.use_case.interactor.attachment

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.AttachmentsListDTO
import com.doneit.ascend.domain.entity.dto.CreateAttachmentDTO
import com.doneit.ascend.domain.use_case.gateway.IAttachmentGateway

internal class AttachmentInteractor(
    private val attachmentGateway: IAttachmentGateway
) : AttachmentUseCase {
    override suspend fun delete(id: Long): ResponseEntity<Unit, List<String>> {
        return attachmentGateway.delete(id)
    }

    override fun getAttachmentListPagedLive(): LiveData<PagedList<AttachmentEntity>> {
        return attachmentGateway.getAttachmentsPagedList(AttachmentsListDTO())
    }

    override suspend fun getAttachmentListPaged(dto: AttachmentsListDTO): PagedList<AttachmentEntity> {
        return attachmentGateway.getAttachments(dto)
    }

    override suspend fun createAttachment(dto: CreateAttachmentDTO): ResponseEntity<AttachmentEntity, List<String>> {
        return attachmentGateway.createAttachment(dto)
    }
}