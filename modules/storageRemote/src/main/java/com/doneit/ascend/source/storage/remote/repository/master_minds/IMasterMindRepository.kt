package com.doneit.ascend.source.storage.remote.repository.master_minds

import com.doneit.ascend.source.storage.remote.data.request.MasterMindListRequest
import com.doneit.ascend.source.storage.remote.data.response.MasterMindListResponse
import com.doneit.ascend.source.storage.remote.data.response.MasterMindResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.data.response.user.UserProfileResponse

interface IMasterMindRepository {

    suspend fun getMasterMindsList(listRequest: MasterMindListRequest): RemoteResponse<MasterMindListResponse, ErrorsListResponse>

    suspend fun getMMProfile(id: Long): RemoteResponse<UserProfileResponse, ErrorsListResponse>

    suspend fun follow(userId: Long): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun unfollow(userId: Long): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun setRatting(userId: Long, rating: Int): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun sendReport(userId: Long, content: String): RemoteResponse<OKResponse, ErrorsListResponse>
}