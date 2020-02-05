package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.response.AttachmentListResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AttachmentsApi {
    @GET("attachments")
    fun getAttachmentsAsync(@Query("page") page: Int?,
                           @Query("per_page") perPage: Int?,
                           @Query("sort_column") sortColumn: String?,
                           @Query("sort_type") sortType: String?,
                           @Query("group_id") groupId: Long?,
                           @Query("user_id") userId: Long?,
                           @Query("file_name") fileName: String?,
                           @Query("link") link: String?,
                           @Query("private") private: Boolean?,
                           @Query("created_at_from") createdAtFrom: String?,
                           @Query("created_at_to") createdAtTo: String?,
                            @Query("updated_at_from") updatedAtFrom: String?,
                            @Query("updated_at_to") updatedAtTo: String?
    ): Deferred<Response<AttachmentListResponse>>

    @DELETE("attachments/{id}")
    fun deleteAttachmentAsync(@Path("id") id: Long): Deferred<Response<OKResponse>>
}