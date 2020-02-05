package com.doneit.ascend.source.storage.remote.repository.attachments

import com.doneit.ascend.source.storage.remote.data.request.AttachmentListRequest
import com.doneit.ascend.source.storage.remote.data.response.AttachmentListResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface IAttachmentsRepository {
    suspend fun getAttachmentsList(listRequest: AttachmentListRequest): RemoteResponse<AttachmentListResponse, ErrorsListResponse>

    suspend fun deleteAttachment(id: Long): RemoteResponse<OKResponse, ErrorsListResponse>
}