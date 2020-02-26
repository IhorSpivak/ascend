package com.doneit.ascend.source.storage.remote.repository.group

import com.doneit.ascend.source.storage.remote.api.GroupApi
import com.doneit.ascend.source.storage.remote.data.request.SubscribeGroupRequest
import com.doneit.ascend.source.storage.remote.data.request.group.CreateGroupRequest
import com.doneit.ascend.source.storage.remote.data.request.group.GroupListRequest
import com.doneit.ascend.source.storage.remote.data.request.group.GroupParticipantsRequest
import com.doneit.ascend.source.storage.remote.data.request.group.UpdateNoteRequest
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.data.response.group.GroupCredentialsResponse
import com.doneit.ascend.source.storage.remote.data.response.group.GroupListResponse
import com.doneit.ascend.source.storage.remote.data.response.group.GroupResponse
import com.doneit.ascend.source.storage.remote.data.response.group.ParticipantListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

internal class GroupRepository(
    gson: Gson,
    private val api: GroupApi
) : BaseRepository(gson), IGroupRepository {

    override suspend fun createGroup(
        file: File,
        request: CreateGroupRequest
    ): RemoteResponse<GroupResponse, ErrorsListResponse> {
        return execute({

            var builder = MultipartBody.Builder()

            var stringPart = MultipartBody.Part.createFormData("fullName", request.name)
            builder = builder.addPart(stringPart)

            stringPart = MultipartBody.Part.createFormData("description", request.description)
            builder = builder.addPart(stringPart)

            stringPart = MultipartBody.Part.createFormData("start_time", request.startTime)
            builder = builder.addPart(stringPart)

            stringPart = MultipartBody.Part.createFormData("group_type", request.groupType)
            builder = builder.addPart(stringPart)

            request.days?.forEach {
                stringPart = MultipartBody.Part.createFormData("wdays[]", it.toString())
                builder = builder.addPart(stringPart)
            }

            stringPart = MultipartBody.Part.createFormData(
                "meetings_count",
                Gson().toJson(request.meetingsCount)
            )
            builder = builder.addPart(stringPart)

            stringPart = MultipartBody.Part.createFormData("price", request.price.toString())
            builder = builder.addPart(stringPart)


            request.participants?.forEach {
                stringPart = MultipartBody.Part.createFormData("participants[]", it)
                builder = builder.addPart(stringPart)
            }

            val filePart = MultipartBody.Part.createFormData(
                "image", file.name, RequestBody.create(
                    "image/*".toMediaTypeOrNull(), file
                )
            )
            builder = builder.addPart(filePart)

            api.createGroupAsync(builder.build().parts)
        }, ErrorsListResponse::class.java)
    }

    override suspend fun getGroupsList(listRequest: GroupListRequest): RemoteResponse<GroupListResponse, ErrorsListResponse> {
        return execute({
            api.getGroupsAsync(
                listRequest.page,
                listRequest.perPage,
                listRequest.sortColumn,
                listRequest.sortType,
                listRequest.name,
                listRequest.userId,
                listRequest.groupType,
                listRequest.status,
                listRequest.myGroups,
                listRequest.startTimeFrom,
                listRequest.startTimeTo
            )
        }, ErrorsListResponse::class.java)
    }

    override suspend fun getGroupDetails(groupId: Long): RemoteResponse<GroupResponse, ErrorsListResponse> {
        return execute({ api.getGroupDetailsAsync(groupId) }, ErrorsListResponse::class.java)
    }

    override suspend fun deleteGroup(groupId: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.deleteGroupAsync(groupId) }, ErrorsListResponse::class.java)
    }

    override suspend fun subscribe(
        groupId: Long,
        request: SubscribeGroupRequest
    ): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.subscribeAsync(groupId, request) }, ErrorsListResponse::class.java)
    }

    override suspend fun getCredentials(groupId: Long): RemoteResponse<GroupCredentialsResponse, ErrorsListResponse> {
        return execute({ api.getCredentialsAsync(groupId) }, ErrorsListResponse::class.java)
    }

    override suspend fun getParticipants(
        groupId: Long,
        request: GroupParticipantsRequest
    ): RemoteResponse<ParticipantListResponse, ErrorsListResponse> {
        return execute({
            api.getParticipantsAsync(
                groupId,
                request.page,
                request.perPage,
                request.sortColumn,
                request.sortType,
                request.fullName,
                request.connected
            )
        }, ErrorsListResponse::class.java)
    }

    override suspend fun updateNote(
        groupId: Long,
        request: UpdateNoteRequest
    ): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.updateNote(groupId, request) }, ErrorsListResponse::class.java)
    }
}


