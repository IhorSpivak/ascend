package com.doneit.ascend.source.storage.remote.data.response

import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.google.gson.annotations.SerializedName

class AttachmentListResponse(
    count: Int,
    @SerializedName("attachments") val attachments: List<AttachmentResponse>
): PagedResponse(count)