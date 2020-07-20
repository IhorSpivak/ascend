package com.doneit.ascend.source.storage.remote.data.response.community_feed

import com.doneit.ascend.source.storage.remote.data.response.CommentResponse
import com.doneit.ascend.source.storage.remote.data.response.base.PagedResponse
import com.google.gson.annotations.SerializedName

class CommentsResponse(
    count: Int? = null,
    @SerializedName("comments")
    val comments: List<CommentResponse>? = null
): PagedResponse(count)