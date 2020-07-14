package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.request.LeaveCommentRequest
import com.doneit.ascend.source.storage.remote.data.request.community_feed.SharePostRequest
import com.doneit.ascend.source.storage.remote.data.response.CommentResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.CommentsResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.PostResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.PostsResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface CommunityFeedApi {
    @GET("posts")
    fun getPostsAsync(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("sort_column") sortColumn: String?,
        @Query("sort_type") sortType: String?,
        @Query("created_at_from") createdAtFrom: String?,
        @Query("created_at_to") createdAtTo: String?,
        @Query("updated_at_from") updatedAtFrom: String?,
        @Query("updated_at_to") updatedAtTo: String?
    ): Deferred<Response<PostsResponse>>

    @POST("posts/{post_id}/likes")
    fun likePostAsync(@Path("post_id") postId: Long): Deferred<Response<PostResponse>>

    @DELETE("posts/{post_id}/likes")
    fun unlikePostAsync(@Path("post_id") postId: Long): Deferred<Response<PostResponse>>

    @POST("posts/{post_id}/comments")
    fun leaveCommentAsync(
        @Path("post_id") postId: Long,
        @Body request: LeaveCommentRequest
    ): Deferred<Response<CommentResponse>>

    @Multipart
    @POST("posts")
    fun createPostAsync(
        @Part attachments: List<MultipartBody.Part>
    ): Deferred<Response<PostResponse>>

    @GET("posts/{post_id}/comments")
    fun getCommentsAsync(
        @Path("post_id") postId: Long,
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("sort_column") sortColumn: String?,
        @Query("sort_type") sortType: String?,
        @Query("created_at_from") createdAtFrom: String?,
        @Query("created_at_to") createdAtTo: String?,
        @Query("updated_at_from") updatedAtFrom: String?,
        @Query("updated_at_to") updatedAtTo: String?,
        @Query("text") text: String?
    ): Deferred<Response<CommentsResponse>>

    @DELETE("posts/{post_id}/comments/{comment_id}")
    fun deleteCommentAsync(
        @Path("post_id") postId: Long,
        @Path("comment_id") commentId: Long
    ): Deferred<Response<OKResponse>>

    @DELETE("posts/{post_id}")
    fun deletePostAsync(
        @Path("post_id") postId: Long
    ): Deferred<Response<OKResponse>>


    @Multipart
    @PATCH("posts/{post_id}")
    fun updatePostAsync(
        @Path("post_id") postId: Long,
        @Query("delete_attachment_ids[]") deleted: Array<String>,
        @Part attachments: List<MultipartBody.Part>
    ): Deferred<Response<PostResponse>>

    @POST(" /api/v1/posts/{post_id}/share")
    fun sharePost(@Path("post_id") postId: Long, @Body sharePostRequest: SharePostRequest): Deferred<Response<OKResponse>>
}