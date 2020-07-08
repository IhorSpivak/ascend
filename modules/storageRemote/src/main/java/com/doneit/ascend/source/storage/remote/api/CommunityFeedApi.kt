package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.request.LeaveCommentRequest
import com.doneit.ascend.source.storage.remote.data.response.CommentResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.PostResponse
import com.doneit.ascend.source.storage.remote.data.response.community_feed.PostsResponse
import kotlinx.coroutines.Deferred
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

    @POST("posts")
    fun createPostAsync(

    )
}