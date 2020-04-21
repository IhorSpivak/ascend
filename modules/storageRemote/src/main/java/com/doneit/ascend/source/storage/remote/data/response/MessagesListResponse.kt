package com.doneit.ascend.source.storage.remote.data.response

import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.google.gson.annotations.SerializedName

class MessagesListResponse(
    count: Int,
    @SerializedName("messages") val messages: List<MessageResponse>?
) : PagedResponse(count)