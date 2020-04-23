package com.doneit.ascend.source.storage.remote.data.response

import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.google.gson.annotations.SerializedName

class BlockedUsersResponse (
    count: Int,
    @SerializedName("blocked_users") val blockedUsers: List<BlockedUserResponse>?
) : PagedResponse(count)