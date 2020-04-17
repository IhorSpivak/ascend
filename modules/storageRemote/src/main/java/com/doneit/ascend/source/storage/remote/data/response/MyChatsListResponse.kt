package com.doneit.ascend.source.storage.remote.data.response

import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.google.gson.annotations.SerializedName

class MyChatsListResponse(
    count: Int,
    @SerializedName("chats") val chats: List<ChatResponse>?
) : PagedResponse(count)