package com.doneit.ascend.source.storage.remote.repository.group

import com.doneit.ascend.source.storage.remote.api.GroupApi
import com.doneit.ascend.source.storage.remote.data.request.CreateGroupRequest
import com.doneit.ascend.source.storage.remote.data.response.GroupResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class GroupRepository(
    gson: Gson,
    private val api: GroupApi
) : BaseRepository(gson), IGroupRepository {

    override suspend fun createGroup(request: CreateGroupRequest): RemoteResponse<GroupResponse, ErrorsListResponse> {
        return execute({ api.createGroupAsync(request)}, ErrorsListResponse::class.java)
    }
}