package com.doneit.ascend.domain.gateway.gateway.boundaries

import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.dto.AttachmentsListDTO
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.local.repository.attachments.IAttachmentRepository
import com.doneit.ascend.source.storage.remote.repository.attachments.IAttachmentsRepository
import com.vrgsoft.core.gateway.orZero
import kotlinx.coroutines.CoroutineScope

class AttachmentBoundaryCallback(
    scope: CoroutineScope,
    private val local: IAttachmentRepository,
    private val remote: IAttachmentsRepository,
    private val attachmentListDTO: AttachmentsListDTO
) : BaseBoundary<AttachmentEntity>(scope) {

    override suspend fun fetchPage() {
        val response = remote.getAttachmentsList(attachmentListDTO.toRequest(pageIndexToLoad))
        if (response.isSuccessful) {
            val model = response.successModel!!.attachments.map { it.toEntity() }

            val loadedCount = model.size
            val remoteCount = response.successModel!!.count

            receivedItems(loadedCount, remoteCount.orZero())

            local.insertAll(model.map { it.toLocal() })
        }
    }
}