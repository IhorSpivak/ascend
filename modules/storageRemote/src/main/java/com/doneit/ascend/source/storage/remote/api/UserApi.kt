package com.doneit.ascend.source.storage.remote.api

import com.doneit.ascend.source.storage.remote.data.request.*
import com.doneit.ascend.source.storage.remote.data.response.AuthResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.ProfileResponse
import com.doneit.ascend.source.storage.remote.data.response.RatesResponse
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface UserApi {

    @POST("sessions")
    fun signIn(@Body request: LogInRequest): Deferred<Response<AuthResponse>>

    @POST("sessions/social_login")
    fun socialSignInAsync(@Body request: SocialLoginRequest): Deferred<Response<AuthResponse>>

    @DELETE("sessions/logout")
    fun signOut(): Deferred<Response<OKResponse>>

    @POST("users")
    fun signUp(@Body request: SignUpRequest): Deferred<Response<AuthResponse>>

    @DELETE("users/profile")
    fun deleteAccount(): Deferred<Response<OKResponse>>

    @GET("users/profile")
    fun getProfileAsync(): Deferred<Response<ProfileResponse>>

    @PUT("users/profile")
    @Multipart
    fun updateProfileAsync(@Part part: List<MultipartBody.Part>): Deferred<Response<ProfileResponse>>

    @POST("users/validation")
    fun signUpValidation(@Body request: SignUpRequest): Deferred<Response<OKResponse>>

    @POST("users/get_code")
    fun getConfirmationCode(@Body request: PhoneRequest): Deferred<Response<OKResponse>>

    @POST("users/forgot_password")
    fun forgotPassword(@Body request: PhoneRequest): Deferred<Response<OKResponse>>

    @POST("users/reset_password")
    fun resetPassword(@Body request: ResetPasswordRequest): Deferred<Response<OKResponse>>

    @POST("users/change_password")
    fun changePasswordAsync(@Body request: ChangePasswordRequest): Deferred<Response<OKResponse>>

    @POST("users/{id}/report")
    fun report(@Query("content") report: String, @Path("id") id: Long): Deferred<Response<OKResponse>>

    @GET("ratings")
    fun getRatingsAsync(@Query("page") page: Int?,
                        @Query("per_page") perPage: Int?,
                        @Query("sort_column") sortColumn: String?,
                        @Query("sort_type") sortType: String?,
                        @Query("full_name") fullName: String?,
                        @Query("created_at_from") createdAtFrom: String?,
                        @Query("created_at_to") createdAtTo: String?,
                        @Query("updated_at_from") updatedAtFrom: String?,
                        @Query("updated_at_to") updatedAtTo: String?
    ): Deferred<Response<RatesResponse>>

    @POST("users/change_phone_number")
    fun changePhone(@Body request: ChangePhoneRequest): Deferred<Response<OKResponse>>

    @POST("users/change_email")
    fun changeEmail(@Body request: ChangeEmailRequest): Deferred<Response<OKResponse>>
}