package com.doneit.ascend.source.storage.remote.repository.group

import com.doneit.ascend.source.storage.remote.data.request.SearchUserRequest
import com.doneit.ascend.source.storage.remote.data.request.SubscribeGroupRequest
import com.doneit.ascend.source.storage.remote.data.request.group.*
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.SearchUserListResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.data.response.group.GroupCredentialsResponse
import com.doneit.ascend.source.storage.remote.data.response.group.GroupListResponse
import com.doneit.ascend.source.storage.remote.data.response.group.GroupResponse
import com.doneit.ascend.source.storage.remote.data.response.group.ParticipantListResponse
import java.io.File

interface IGroupRepository {
    suspend fun createGroup(file: File, request: CreateGroupRequest): RemoteResponse<GroupResponse, ErrorsListResponse>

    suspend fun updateGroup(id: Long, file: File, request: CreateGroupRequest): RemoteResponse<GroupResponse, ErrorsListResponse>

    suspend fun getGroupsList(listRequest: GroupListRequest): RemoteResponse<GroupListResponse, ErrorsListResponse>

    suspend fun getGroupDetails(groupId: Long): RemoteResponse<GroupResponse, ErrorsListResponse>

    suspend fun deleteGroup(groupId: Long): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun leaveGroup(groupId: Long): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun deleteInvite(groupId: Long, inviteId: Long): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun subscribe(
        groupId: Long,
        request: SubscribeGroupRequest
    ): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun getCredentials(groupId: Long): RemoteResponse<GroupCredentialsResponse, ErrorsListResponse>

    suspend fun getParticipants(groupId: Long, request: GroupParticipantsRequest): RemoteResponse<ParticipantListResponse, ErrorsListResponse>

    suspend fun updateNote(groupId: Long, request: UpdateNoteRequest): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun cancelGroup(groupId: Long, request: CancelGroupRequest): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun inviteToGroup(groupId: Long, request: InviteToGroupRequest): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun searchUsers(searchRequest: SearchUserRequest): RemoteResponse<SearchUserListResponse, ErrorsListResponse>
}