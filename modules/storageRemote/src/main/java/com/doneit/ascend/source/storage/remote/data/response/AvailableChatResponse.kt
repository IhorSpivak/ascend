package com.doneit.ascend.source.storage.remote.data.response

import com.google.gson.annotations.SerializedName

class AvailableChatResponse (
    @SerializedName("chat_ids") val chatIds: List<Long>?
)