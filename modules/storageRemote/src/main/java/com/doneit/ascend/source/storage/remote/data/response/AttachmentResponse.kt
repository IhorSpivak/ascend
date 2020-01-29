package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class AttachmentResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("file_name") val fileName: String,
    @SerializedName("file_size") val fileSize: Long,
    @SerializedName("link") val link: String,
    @SerializedName("group_id") val groupId: Long,
    @SerializedName("user_id") val userId: Long,
    @SerializedName("private") val private: Boolean,
    @SerializedName("attachment_type") val attachmentType: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)