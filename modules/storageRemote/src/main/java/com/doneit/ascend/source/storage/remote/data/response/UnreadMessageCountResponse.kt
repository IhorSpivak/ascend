package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

data class UnreadMessageCountResponse(
    @SerializedName("unread_messages_count") val unreadMessageCount: Long
)