package com.doneit.ascend.source.storage.remote.repository.group

import com.doneit.ascend.source.storage.remote.data.request.CreateGroupRequest
import com.doneit.ascend.source.storage.remote.data.request.GroupListRequest
import com.doneit.ascend.source.storage.remote.data.response.GroupListResponse
import com.doneit.ascend.source.storage.remote.data.response.GroupResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import java.io.File

interface IGroupRepository {
    suspend fun createGroup(file: File, request: CreateGroupRequest): RemoteResponse<GroupResponse, ErrorsListResponse>

    suspend fun getGroupsList(listRequest: GroupListRequest): RemoteResponse<GroupListResponse, ErrorsListResponse>

    suspend fun getGroupDetails(groupId: Long): RemoteResponse<GroupResponse, ErrorsListResponse>

    suspend fun deleteGroup(groupId: Long) : RemoteResponse<OKResponse, ErrorsListResponse>
}