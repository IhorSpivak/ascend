package com.doneit.ascend.source.storage.remote.repository.attachments

import com.doneit.ascend.source.storage.remote.api.AttachmentsApi
import com.doneit.ascend.source.storage.remote.data.request.AttachmentListRequest
import com.doneit.ascend.source.storage.remote.data.response.AttachmentListResponse
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
}