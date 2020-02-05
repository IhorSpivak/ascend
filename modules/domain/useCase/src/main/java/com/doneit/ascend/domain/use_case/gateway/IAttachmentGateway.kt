package com.doneit.ascend.domain.use_case.gateway

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.AttachmentsListModel

interface IAttachmentGateway {
    suspend fun getAttachments(listModel: AttachmentsListModel): PagedList<AttachmentEntity>
    fun getAttachmentsPagedList(listModel: AttachmentsListModel): LiveData<PagedList<AttachmentEntity>>
    suspend fun delete(id: Long): ResponseEntity<Unit, List<String>>
}