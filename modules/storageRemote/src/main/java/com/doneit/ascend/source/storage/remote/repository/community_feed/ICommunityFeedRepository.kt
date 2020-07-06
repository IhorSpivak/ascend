package com.doneit.ascend.source.storage.remote.repository.community_feed

import com.doneit.ascend.source.storage.remote.data.request.community_feed.PostsRequest
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.PostsResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface ICommunityFeedRepository {
    suspend fun loadPosts(postsRequest: PostsRequest): RemoteResponse<PostsResponse, ErrorsListResponse>
}