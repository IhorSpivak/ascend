package com.doneit.ascend.source.storage.remote.data.request.community_feed

import com.google.gson.annotations.SerializedName

class ShareRequest(
    @SerializedName("chat_ids") val chatIds: List<Long>?,
    @SerializedName("user_ids") val userIds: List<Long>?
)