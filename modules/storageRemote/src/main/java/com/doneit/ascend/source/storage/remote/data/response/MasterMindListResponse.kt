package com.doneit.ascend.source.storage.remote.data.response

import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.google.gson.annotations.SerializedName

class MasterMindListResponse(
    count: Int,
    @SerializedName("users") val users: List<MasterMindResponse>
): PagedResponse(count)