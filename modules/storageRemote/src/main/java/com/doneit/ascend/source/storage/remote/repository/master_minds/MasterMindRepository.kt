package com.doneit.ascend.source.storage.remote.repository.master_minds

import com.doneit.ascend.source.storage.remote.api.MasterMindApi
import com.doneit.ascend.source.storage.remote.data.request.MasterMindListRequest
import com.doneit.ascend.source.storage.remote.data.request.RatingRequest
import com.doneit.ascend.source.storage.remote.data.request.ReportRequest
import com.doneit.ascend.source.storage.remote.data.response.MasterMindListResponse
import com.doneit.ascend.source.storage.remote.data.response.MasterMindResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.data.response.user.UserProfileResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class MasterMindRepository(
    gson: Gson,
    private val api: MasterMindApi
) : BaseRepository(gson), IMasterMindRepository {

    override suspend fun getMasterMindsList(listRequest: MasterMindListRequest): RemoteResponse<MasterMindListResponse, ErrorsListResponse> {
        return execute({
            api.getMasterMindsListAsync(
                listRequest.page,
                listRequest.perPage,
                listRequest.sortColumn,
                listRequest.sortType,
                listRequest.fullName,
                listRequest.displayName,
                listRequest.followed,
                listRequest.rated
            )
        }, ErrorsListResponse::class.java)
    }

    override suspend fun getMMProfile(id: Long): RemoteResponse<UserProfileResponse, ErrorsListResponse> {
        return execute({ api.getProfileAsync(id) }, ErrorsListResponse::class.java)
    }

    override suspend fun follow(userId: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.followAsync(userId) }, ErrorsListResponse::class.java)
    }

    override suspend fun unfollow(userId: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.unfollowAsync(userId) }, ErrorsListResponse::class.java)
    }

    override suspend fun setRatting(
        userId: Long,
        rating: Int
    ): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.setRatingAsync(userId, RatingRequest(rating)) }, ErrorsListResponse::class.java)
    }

    override suspend fun sendReport(
        userId: Long,
        content: String
    ): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.sendReportAsync(userId, ReportRequest(content)) }, ErrorsListResponse::class.java)
    }
}