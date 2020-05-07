package com.doneit.ascend.source.storage.remote.repository.group

import com.doneit.ascend.source.storage.remote.api.GroupApi
import com.doneit.ascend.source.storage.remote.api.UserApi
import com.doneit.ascend.source.storage.remote.data.request.SearchUserRequest
import com.doneit.ascend.source.storage.remote.data.request.SubscribeGroupRequest
import com.doneit.ascend.source.storage.remote.data.request.group.*
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.SearchUserListResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.data.response.group.*
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

internal class GroupRepository(
    gson: Gson,
    private val api: GroupApi,
    private val userApi: UserApi
) : BaseRepository(gson), IGroupRepository {

    override suspend fun createGroup(
        file: File,
        request: CreateGroupRequest
    ): RemoteResponse<GroupResponse, ErrorsListResponse> {
        return execute({
            val builder = MultipartBody.Builder().apply {
                addPart(MultipartBody.Part.createFormData("name", request.name ?: ""))
                addPart(MultipartBody.Part.createFormData("description", request.description ?: ""))
                addPart(MultipartBody.Part.createFormData("start_time", request.startTime ?: ""))
                addPart(MultipartBody.Part.createFormData("group_type", request.groupType ?: ""))
                when (request.groupType) {
                    MASTER_MIND -> {
                    }
                    INDIVIDUAL -> {
                    }
                    SUPPORT -> {
                        addPart(
                            MultipartBody.Part.createFormData(
                                "tag_id",
                                request.tagId.toString()
                            )
                        )
                    }
                    WEBINAR -> {
                        request.times?.forEach { time ->
                            addPart(
                                MultipartBody.Part.createFormData(
                                    "times[]",
                                    time
                                )
                            )
                        }
                        request.themes?.forEach { theme ->
                            addPart(
                                MultipartBody.Part.createFormData(
                                    "themes[]",
                                    theme
                                )
                            )
                        }
                    }
                }
                if (request.private!!) {
                    addPart(MultipartBody.Part.createFormData("private", "1"))
                } else {
                    addPart(MultipartBody.Part.createFormData("private", "0"))
                }
                request.days?.forEach {
                    addPart(MultipartBody.Part.createFormData("wdays[]", it.toString()))
                }
                addPart(
                    MultipartBody.Part.createFormData(
                        "meetings_count",
                        Gson().toJson(request.meetingsCount)
                    )
                )
                addPart(MultipartBody.Part.createFormData("price", request.price.toString()))
                request.participants?.forEach { email ->
                    addPart(
                        MultipartBody.Part.createFormData(
                            "participants[]",
                            email
                        )
                    )
                }
                addPart(
                    MultipartBody.Part.createFormData(
                        "image",
                        file.name,
                        RequestBody.create("image/*".toMediaTypeOrNull(), file)
                    )
                )
                addPart(
                    MultipartBody.Part.createFormData(
                        "meeting_format",
                        request.meetingFormat ?: ""
                    )
                )
            }
            api.createGroupAsync(builder.build().parts)
        }, ErrorsListResponse::class.java)
    }

    override suspend fun updateGroup(
        id: Long,
        file: File?,
        request: UpdateGroupRequest
    ): RemoteResponse<GroupResponse, ErrorsListResponse> {
        return execute({

            var builder = MultipartBody.Builder()
            request.let {
                if (it.name != null) {
                    builder.addPart(MultipartBody.Part.createFormData("name", it.name))
                }
                if (it.description != null) {
                    builder.addPart(
                        MultipartBody.Part.createFormData(
                            "description",
                            it.description
                        )
                    )
                }
                if (it.startTime != null) {
                    builder.addPart(MultipartBody.Part.createFormData("start_time", it.startTime))
                }
                if (it.groupType != null) {
                    builder.addPart(MultipartBody.Part.createFormData("group_type", it.groupType))
                    if (it.groupType.equals("support", true)) {
                        builder.addPart(
                            MultipartBody.Part.createFormData(
                                "tag_id",
                                it.tagId.toString()
                            )
                        )
                    }
                }
                if (it.private != null) {
                    builder.addPart(
                        MultipartBody.Part.createFormData(
                            "private",
                            it.private.let { isPrivate ->
                                if (isPrivate) {
                                    "1"
                                } else {
                                    "0"
                                }
                            })
                    )
                }
                if (it.days != null) {
                    it.days.forEach { day ->
                        builder.addPart(
                            MultipartBody.Part.createFormData(
                                "wdays[]",
                                day.toString()
                            )
                        )
                    }
                }
                if (it.meetingsCount != null) {
                    builder.addPart(
                        MultipartBody.Part.createFormData(
                            "meetings_count",
                            Gson().toJson(it.meetingsCount)
                        )
                    )
                }
                if (it.price != null) {
                    builder.addPart(MultipartBody.Part.createFormData("price", it.price.toString()))
                }
                it.participants?.forEach { email ->
                    builder.addPart(
                        MultipartBody.Part.createFormData(
                            "participants[]",
                            email
                        )
                    )
                }
                it.removedParticipants?.forEach { email ->
                    builder.addPart(
                        MultipartBody.Part.createFormData(
                            "removed_participants[]",
                            email
                        )
                    )
                }
                if (file != null) {
                    builder.addPart(
                        MultipartBody.Part.createFormData(
                            "image",
                            file.name,
                            RequestBody.create("image/*".toMediaTypeOrNull(), file)
                        )
                    )
                }
                if (it.meetingFormat != null) {
                    builder.addPart(
                        MultipartBody.Part.createFormData(
                            "meeting_format",
                            it.meetingFormat ?: ""
                        )
                    )
                }
                it.times?.let { list ->
                    list.forEach { time ->
                        builder.addPart(
                            MultipartBody.Part.createFormData(
                                "times[]",
                                time
                            )
                        )
                    }
                }
                it.themes?.let { list ->
                    list.forEach { theme ->
                        builder.addPart(
                            MultipartBody.Part.createFormData(
                                "themes[]",
                                theme
                            )
                        )
                    }
                }
            }
            api.updateGroupAsync(id, builder.build().parts)
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
                listRequest.startTimeTo,
                listRequest.community
            )
        }, ErrorsListResponse::class.java)
    }

    override suspend fun getGroupDetails(groupId: Long): RemoteResponse<GroupResponse, ErrorsListResponse> {
        return execute({ api.getGroupDetailsAsync(groupId) }, ErrorsListResponse::class.java)
    }

    override suspend fun deleteGroup(groupId: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.deleteGroupAsync(groupId) }, ErrorsListResponse::class.java)
    }

    override suspend fun leaveGroup(groupId: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.leaveGroupAsync(groupId) }, ErrorsListResponse::class.java)
    }

    override suspend fun deleteInvite(
        groupId: Long,
        inviteId: Long
    ): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.deleteInviteAsync(groupId, inviteId) }, ErrorsListResponse::class.java)
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

    override suspend fun cancelGroup(
        groupId: Long,
        request: CancelGroupRequest
    ): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.cancelGroup(groupId, request) }, ErrorsListResponse::class.java)
    }

    override suspend fun inviteToGroup(
        groupId: Long,
        request: InviteToGroupRequest
    ): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.inviteToGroup(groupId, request) }, ErrorsListResponse::class.java)
    }

    override suspend fun searchUsers(searchRequest: SearchUserRequest): RemoteResponse<SearchUserListResponse, ErrorsListResponse> {
        return execute(
            {
                userApi.searchUsersAsync(
                    searchRequest.page,
                    searchRequest.perPage,
                    searchRequest.sortColumn,
                    searchRequest.sortType,
                    searchRequest.fullName,
                    searchRequest.email
                )
            },
            ErrorsListResponse::class.java
        )
    }

    override suspend fun getTags(): RemoteResponse<TagListResponse, ErrorsListResponse> {
        return execute(
            { api.getTags() }, ErrorsListResponse::class.java
        )
    }

    companion object {
        const val MASTER_MIND = "master_mind"
        const val INDIVIDUAL = "individual"
        const val SUPPORT = "support"
        const val WEBINAR = "webinar"
    }
}


