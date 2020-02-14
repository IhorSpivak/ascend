package com.doneit.ascend.source.storage.remote.repository.attachments

import com.doneit.ascend.source.storage.remote.api.AttachmentsApi
import com.doneit.ascend.source.storage.remote.data.request.attachment.AttachmentListRequest
import com.doneit.ascend.source.storage.remote.data.request.attachment.CreateAttachmentRequest
import com.doneit.ascend.source.storage.remote.data.response.AttachmentListResponse
import com.doneit.ascend.source.storage.remote.data.response.AttachmentResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class AttachmentRepository(
    gson: Gson,
    private val api: AttachmentsApi
) : BaseRepository(gson), IAttachmentsRepository {

    override suspend fun getAttachmentsList(listRequest: AttachmentListRequest): RemoteResponse<AttachmentListResponse, ErrorsListResponse> {
        return execute({
            api.getAttachmentsAsync(
                listRequest.page,
                listRequest.perPage,
                listRequest.sortColumn,
                listRequest.sortType,
                listRequest.groupId,
                listRequest.userId,
                listRequest.fileName,
                listRequest.link,
                listRequest.private,
                listRequest.createdAtFrom,
                listRequest.createdAtTo,
                listRequest.updatedAtFrom,
                listRequest.updatedAtTo
            )
        }, ErrorsListResponse::class.java)
    }

    override suspend fun deleteAttachment(id: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.deleteAttachmentAsync(id) }, ErrorsListResponse::class.java)
    }

    override suspend fun createAttachment(request: CreateAttachmentRequest): RemoteResponse<AttachmentResponse, ErrorsListResponse> {
        return execute({ api.createAttachmentAsync(request) }, ErrorsListResponse::class.java)
    }
}