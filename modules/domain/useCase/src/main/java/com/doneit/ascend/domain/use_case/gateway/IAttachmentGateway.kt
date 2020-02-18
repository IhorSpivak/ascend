package com.doneit.ascend.domain.use_case.gateway

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AttachmentEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.AttachmentsListDTO
import com.doneit.ascend.domain.entity.dto.CreateAttachmentDTO

interface IAttachmentGateway {

    suspend fun getAttachmentList(listDTO: AttachmentsListDTO): PagedList<AttachmentEntity>

    fun getAttachmentListLive(listDTO: AttachmentsListDTO): LiveData<PagedList<AttachmentEntity>>

    suspend fun delete(id: Long): ResponseEntity<Unit, List<String>>

    suspend fun createAttachment(dto: CreateAttachmentDTO): ResponseEntity<AttachmentEntity, List<String>>
}