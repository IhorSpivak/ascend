package com.doneit.ascend.source.storage.remote.data.request.attachment

import com.doneit.ascend.source.storage.remote.data.request.base.BasePagedModel
import com.google.gson.annotations.SerializedName

class AttachmentListRequest(
    page: Int?,
    perPage: Int?,
    sortColumn: String?,
    sortType: String?,
    @SerializedName("group_id") val groupId: Long?,
    @SerializedName("user_id") val userId: Long?,
    @SerializedName("file_name") val fileName: String?,
    @SerializedName("link") val link: String?,
    @SerializedName("private") val private: Boolean?,
    @SerializedName("created_at_from") val createdAtFrom: String?,
    @SerializedName("created_at_to") val createdAtTo: String?,
    @SerializedName("updated_at_from") val updatedAtFrom: String?,
    @SerializedName("updated_at_to") val updatedAtTo: String?
): BasePagedModel(page, perPage, sortColumn, sortType)