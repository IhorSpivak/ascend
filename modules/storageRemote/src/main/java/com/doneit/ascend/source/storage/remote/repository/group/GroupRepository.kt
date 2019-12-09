package com.doneit.ascend.source.storage.remote.repository.group

import com.doneit.ascend.source.storage.remote.api.GroupApi
import com.doneit.ascend.source.storage.remote.data.request.CreateGroupRequest
import com.doneit.ascend.source.storage.remote.data.response.GroupResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

internal class GroupRepository(
    gson: Gson,
    private val api: GroupApi
) : BaseRepository(gson), IGroupRepository {

    override suspend fun createGroup(file: File, request: CreateGroupRequest): RemoteResponse<GroupResponse, ErrorsListResponse> {
        return execute({

            var builder = MultipartBody.Builder()

            var stringPart = MultipartBody.Part.createFormData("name", request.name)
            builder = builder.addPart(stringPart)

            stringPart = MultipartBody.Part.createFormData("description", request.description)
            builder = builder.addPart(stringPart)

            stringPart = MultipartBody.Part.createFormData("start_time", request.startTime)
            builder = builder.addPart(stringPart)

            stringPart = MultipartBody.Part.createFormData("group_type", request.groupType)
            builder = builder.addPart(stringPart)

            stringPart = MultipartBody.Part.createFormData("price", request.price)
            builder = builder.addPart(stringPart)

            stringPart = MultipartBody.Part.createFormData("participants", Gson().toJson(request.participants))
            builder = builder.addPart(stringPart)

            val filePart = MultipartBody.Part.createFormData("image", file.name, RequestBody.create(
                MediaType.parse("image/*"), file))
            builder = builder.addPart(filePart)

            api.createGroupAsync(builder.build().parts())
        }, ErrorsListResponse::class.java)
    }
}


