package com.doneit.ascend.source.storage.remote.repository.community_feed

import com.doneit.ascend.source.storage.remote.data.request.AttachmentRequest
import com.doneit.ascend.source.storage.remote.data.request.community_feed.CommentsRequest
import com.doneit.ascend.source.storage.remote.data.request.community_feed.PostsRequest
import com.doneit.ascend.source.storage.remote.data.request.community_feed.SharePostRequest
import com.doneit.ascend.source.storage.remote.data.response.CommentResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.CommentsResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.PostResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.PostsResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse

interface ICommunityFeedRepository {
    suspend fun loadPosts(postsRequest: PostsRequest): RemoteResponse<PostsResponse, ErrorsListResponse>
    suspend fun likePost(postId: Long): RemoteResponse<PostResponse, ErrorsListResponse>
    suspend fun unlikePost(postId: Long): RemoteResponse<PostResponse, ErrorsListResponse>
    suspend fun leaveComment(
        postId: Long,
        text: String
    ): RemoteResponse<CommentResponse, ErrorsListResponse>

    suspend fun createPost(
        description: String,
        attachments: List<AttachmentRequest>
    ): RemoteResponse<PostResponse, ErrorsListResponse>

    suspend fun getComments(postId: Long, commentsRequest: CommentsRequest): RemoteResponse<CommentsResponse, ErrorsListResponse>

    suspend fun deleteComment(postId: Long, commentId: Long): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun deletePost(postId: Long): RemoteResponse<OKResponse, ErrorsListResponse>

    suspend fun updatePost(
        postId: Long,
        description: String,
        deletedAttachments: Array<Long>,
        attachments: List<AttachmentRequest>
    ): RemoteResponse<PostResponse, ErrorsListResponse>

    suspend fun sharePost(
        postId: Long,
        sharePostRequest: SharePostRequest
    ): RemoteResponse<OKResponse, ErrorsListResponse>
}