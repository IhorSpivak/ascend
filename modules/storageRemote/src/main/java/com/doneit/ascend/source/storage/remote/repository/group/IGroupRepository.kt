package com.doneit.ascend.source.storage.remote.repository.group

import com.doneit.ascend.source.storage.remote.data.request.CreateGroupRequest
import com.doneit.ascend.source.storage.remote.data.response.GroupResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import java.io.File

interface IGroupRepository {
    suspend fun createGroup(file: File, request: CreateGroupRequest): RemoteResponse<GroupResponse, ErrorsListResponse>
}