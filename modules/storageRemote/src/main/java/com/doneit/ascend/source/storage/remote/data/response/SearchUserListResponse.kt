package com.doneit.ascend.source.storage.remote.data.response

import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.doneit.ascend.source.storage.remote.data.response.user.SearchUsersResponse
import com.google.gson.annotations.SerializedName

class SearchUserListResponse (
    count: Int,
    @SerializedName("users") val users: List<SearchUsersResponse>
): PagedResponse(count)