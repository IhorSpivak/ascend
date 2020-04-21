package com.doneit.ascend.source.storage.remote.data.response

import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.google.gson.annotations.SerializedName

class MemberListResponse (
    count: Int,
    @SerializedName("users") val messages: List<MemberResponse>?
) : PagedResponse(count)