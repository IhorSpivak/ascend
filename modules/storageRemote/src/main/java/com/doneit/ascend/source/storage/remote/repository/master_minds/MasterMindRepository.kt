package com.doneit.ascend.source.storage.remote.repository.master_minds

import com.doneit.ascend.source.storage.remote.api.MasterMindApi
import com.doneit.ascend.source.storage.remote.data.request.MasterMindListRequest
import com.doneit.ascend.source.storage.remote.data.response.MasterMindListResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class MasterMindRepository(
    gson: Gson,
    private val api: MasterMindApi
) : BaseRepository(gson), IMasterMindRepository {

    override suspend fun getMasterMindsList(listRequest: MasterMindListRequest): RemoteResponse<MasterMindListResponse, ErrorsListResponse> {
        return execute({
            api.getMasterMindsList(
                listRequest.page,
                listRequest.perPage,
                listRequest.sortColumn,
                listRequest.sortType,
                listRequest.fullName,
                listRequest.displayName,
                listRequest.followed,
                listRequest.rated)
        }, ErrorsListResponse::class.java)
    }
}