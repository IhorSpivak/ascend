package com.doneit.ascend.source.storage.remote.data.request.attachment

import com.google.gson.annotations.SerializedName

data class CreateAttachmentRequest(
    @SerializedName("group_id") val groupId: Long,
    @SerializedName("file_name") val fileName: String?,
    @SerializedName("file_size") val fileSize: Long?,
    @SerializedName("link") val link: String,
    @SerializedName("attachment_type") val attachmentType: String,
    @SerializedName("private") val private: Boolean
)