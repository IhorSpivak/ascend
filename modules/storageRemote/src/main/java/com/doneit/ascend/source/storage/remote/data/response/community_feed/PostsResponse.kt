package com.doneit.ascend.source.storage.remote.data.response.community_feed


import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.google.gson.annotations.SerializedName

class PostsResponse(
    count: Int? = null,
    @SerializedName("posts")
    val posts: List<PostResponse>? = null
):PagedResponse(count)