package com.doneit.ascend.source.storage.remote.data.response

import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.google.gson.annotations.SerializedName

class GroupListResponse(
    count: Int,
    @SerializedName("groups") val groups: List<GroupResponse>
): PagedResponse(count)