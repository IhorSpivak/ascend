package com.doneit.ascend.source.storage.remote.data.response.group

import com.doneit.ascend.source.storage.remote.data.response.TagResponse
import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.google.gson.annotations.SerializedName

class TagListResponse(
    count: Int,
    @SerializedName("tags") val tags: List<TagResponse>
) : PagedResponse(count)