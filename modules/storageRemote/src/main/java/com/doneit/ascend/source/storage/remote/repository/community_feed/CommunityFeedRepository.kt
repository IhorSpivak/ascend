package com.doneit.ascend.source.storage.remote.repository.community_feed

import com.doneit.ascend.source.storage.remote.api.CommunityFeedApi
import com.doneit.ascend.source.storage.remote.data.request.LeaveCommentRequest
import com.doneit.ascend.source.storage.remote.data.request.community_feed.PostsRequest
import com.doneit.ascend.source.storage.remote.data.response.CommentResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.PostResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.PostsResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson

internal class CommunityFeedRepository(
    gson: Gson,
    private val api: CommunityFeedApi
) : BaseRepository(gson), ICommunityFeedRepository {
    override suspend fun loadPosts(postsRequest: PostsRequest): RemoteResponse<PostsResponse, ErrorsListResponse> {
        return execute({
            api.getPostsAsync(
                page = postsRequest.page,
                perPage = postsRequest.perPage,
                sortColumn = postsRequest.sortColumn,
                sortType = postsRequest.sortType,
                createdAtFrom = postsRequest.createdAtFrom,
                createdAtTo = postsRequest.createdAtTo,
                updatedAtFrom = postsRequest.updatedAtFrom,
                updatedAtTo = postsRequest.updatedAtTo
            )
        }, ErrorsListResponse::class.java)
    }

    override suspend fun likePost(postId: Long): RemoteResponse<PostResponse, ErrorsListResponse> {
        return execute({
            api.likePostAsync(postId)
        }, ErrorsListResponse::class.java)
    }

    override suspend fun unlikePost(postId: Long): RemoteResponse<PostResponse, ErrorsListResponse> {
        return execute({
            api.unlikePostAsync(postId)
        }, ErrorsListResponse::class.java)
    }

    override suspend fun leaveComment(
        postId: Long,
        text: String
    ): RemoteResponse<CommentResponse, ErrorsListResponse> {
        return execute({
            api.leaveCommentAsync(postId, LeaveCommentRequest(text))
        }, ErrorsListResponse::class.java)
    }
}