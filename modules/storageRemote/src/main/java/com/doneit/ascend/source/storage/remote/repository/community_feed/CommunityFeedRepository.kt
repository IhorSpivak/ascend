package com.doneit.ascend.source.storage.remote.repository.community_feed

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.doneit.ascend.source.storage.remote.api.CommunityFeedApi
import com.doneit.ascend.source.storage.remote.data.request.AttachmentRequest
import com.doneit.ascend.source.storage.remote.data.request.LeaveCommentRequest
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
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

internal class CommunityFeedRepository(
    gson: Gson,
    private val contentResolver: ContentResolver,
    private val context: Context,
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
                addAttachments(attachments)
                api.createPostAsync(build().parts)
            }
        }, ErrorsListResponse::class.java)
    }

    override suspend fun updatePost(
        postId: Long,
        description: String,
        deletedAttachments: Array<Long>,
        attachments: List<AttachmentRequest>
    ): RemoteResponse<PostResponse, ErrorsListResponse> {
        return execute({
            with(MultipartBody.Builder()) {
                addPart(MultipartBody.Part.createFormData("description", description))
                addAttachments(attachments)
                api.updatePostAsync(postId, deletedAttachments, build().parts)
            }
        }, ErrorsListResponse::class.java)
    }

    override suspend fun sharePost(
        postId: Long,
        sharePostRequest: SharePostRequest
    ): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({
            api.sharePost(postId, sharePostRequest)
        }, ErrorsListResponse::class.java)
    }

    private fun MultipartBody.Builder.addAttachments(attachments: List<AttachmentRequest>) {
        for (attachment in attachments.filter { !it.url.startsWith("http") }.withIndex()) {
            val inputStream = contentResolver.openInputStream(
                Uri.parse(attachment.value.url)
            )!!
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
                            getContentType(attachment.value)
                                .substringAfterLast("/"),
                    body
                )
            )
        }
    }

    private fun getContentType(attachment: AttachmentRequest): String {
        return contentResolver.getType(Uri.parse(attachment.url)) ?: contentResolver.getType(
            FileProvider.getUriForFile(
                context,
                context.packageName + ".fileprovider",
                File(attachment.url)
            )
        ).toString()
    }

    override suspend fun getComments(
        postId: Long,
        commentsRequest: CommentsRequest
    ): RemoteResponse<CommentsResponse, ErrorsListResponse> {
        return execute({
            api.getCommentsAsync(
                postId = postId,
                page = commentsRequest.page,
                perPage = commentsRequest.perPage,
                sortColumn = commentsRequest.sortColumn,
                sortType = commentsRequest.sortType,
                createdAtFrom = commentsRequest.createdAtFrom,
                createdAtTo = commentsRequest.createdAtTo,
                updatedAtFrom = commentsRequest.updatedAtFrom,
                updatedAtTo = commentsRequest.updatedAtTo,
                text = commentsRequest.text
            )
        }, ErrorsListResponse::class.java)
    }

    override suspend fun deleteComment(
        postId: Long,
        commentId: Long
    ): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({
            api.deleteCommentAsync(postId, commentId)
        }, ErrorsListResponse::class.java)
    }

    override suspend fun deletePost(postId: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({
            api.deletePostAsync(postId)
        }, ErrorsListResponse::class.java)
    }

}