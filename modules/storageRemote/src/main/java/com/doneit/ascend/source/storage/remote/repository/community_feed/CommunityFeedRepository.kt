package com.doneit.ascend.source.storage.remote.repository.community_feed

import android.content.ContentResolver
import android.net.Uri
import com.doneit.ascend.source.storage.remote.api.CommunityFeedApi
import com.doneit.ascend.source.storage.remote.data.request.AttachmentRequest
import com.doneit.ascend.source.storage.remote.data.request.LeaveCommentRequest
import com.doneit.ascend.source.storage.remote.data.request.community_feed.PostsRequest
import com.doneit.ascend.source.storage.remote.data.response.CommentResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.PostResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.PostsResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*

internal class CommunityFeedRepository(
    gson: Gson,
    private val contentResolver: ContentResolver,
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

    override suspend fun createPost(
        description: String,
        attachments: List<AttachmentRequest>
    ): RemoteResponse<PostResponse, ErrorsListResponse> {
        return execute({
            with(MultipartBody.Builder()) {
                addPart(MultipartBody.Part.createFormData("description", description))
                for (attachment in attachments.withIndex()) {
                    val inputStream =
                        contentResolver.openInputStream(Uri.parse(attachment.value.url))!!
                    val bytes = inputStream.use {
                        it.readBytes()
                    }
                    val body = bytes.toRequestBody(
                        contentType = attachment.value.contentType
                            .toMediaTypeOrNull()
                    )
                    addPart(
                        MultipartBody.Part.createFormData(
                            "attachment${attachment.index + 1}",
                            UUID.randomUUID().toString() + "." +
                                    contentResolver.getType(Uri.parse(attachment.value.url))!!
                                        .substringAfterLast("/"),
                            body
                        )
                    )
                }
                api.createPostAsync(build().parts)
            }
        }, ErrorsListResponse::class.java)
    }

}