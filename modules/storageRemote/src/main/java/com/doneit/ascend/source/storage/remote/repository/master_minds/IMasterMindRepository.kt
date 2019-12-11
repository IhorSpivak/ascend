package com.doneit.ascend.source.storage.remote.repository.master_minds

import com.doneit.ascend.source.storage.remote.data.request.MasterMindListRequest
import com.doneit.ascend.source.storage.remote.data.response.MasterMindListResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface IMasterMindRepository {

    suspend fun getMasterMindsList(listRequest: MasterMindListRequest): RemoteResponse<MasterMindListResponse, ErrorsListResponse>
}