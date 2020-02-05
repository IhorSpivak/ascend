package com.doneit.ascend.domain.use_case.interactor.attachment

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.MasterMindEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.AttachmentsListModel

interface AttachmentUseCase {
    suspend fun getAttachmentListPaged(model: AttachmentsListModel): PagedList<AttachmentEntity>
    fun getAttachmentListPagedLive(): LiveData<PagedList<AttachmentEntity>>
    suspend fun delete(id: Long): ResponseEntity<Unit, List<String>>
}