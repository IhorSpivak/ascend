package com.doneit.ascend.source.storage.remote.repository.user

import com.doneit.ascend.source.storage.remote.api.UserApi
import com.doneit.ascend.source.storage.remote.data.request.*
import com.doneit.ascend.source.storage.remote.data.response.AuthResponse
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.ProfileResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

internal class UserRepository(
    gson: Gson,
    private val api: UserApi
) : BaseRepository(gson),
    IUserRepository {

    override suspend fun signIn(request: LogInRequest): RemoteResponse<AuthResponse, ErrorsListResponse> {
        return execute({ api.signIn(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun signOut(): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.signOut() }, ErrorsListResponse::class.java)
    }

    override suspend fun socialSignIn(request: SocialLoginRequest): RemoteResponse<AuthResponse, ErrorsListResponse> {
        return execute({ api.socialSignInAsync(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun signUp(request: SignUpRequest): RemoteResponse<AuthResponse, ErrorsListResponse> {
        return execute({ api.signUp(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun deleteAccount(): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.deleteAccount() }, ErrorsListResponse::class.java)
    }

    override suspend fun signUpValidation(request: SignUpRequest): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.signUpValidation(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun getConfirmationCode(request: PhoneRequest): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.getConfirmationCode(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun forgotPassword(request: PhoneRequest): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.forgotPassword(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.resetPassword(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun report(content: String, id: Long): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({api.report(content,id)}, ErrorsListResponse::class.java)
    }

    override suspend fun getProfile(): RemoteResponse<ProfileResponse, ErrorsListResponse> {
        return execute({ api.getProfileAsync() }, ErrorsListResponse::class.java)
    }

    override suspend fun updateProfile(
        file: File?,
        request: UpdateProfileRequest,
        updateImage: Boolean
    ): RemoteResponse<ProfileResponse, ErrorsListResponse> {
        return execute({

            val serializer = GsonBuilder().serializeNulls().create()//todo resolve null passing problem

            var builder = MultipartBody.Builder()

            var stringPart: MultipartBody.Part
            request.fullName?.let {
                stringPart = MultipartBody.Part.createFormData("full_name", request.fullName)
                builder = builder.addPart(stringPart)
            }

            stringPart = MultipartBody.Part.createFormData("display_name", serializer.toJson(request.displayName))
            builder = builder.addPart(stringPart)

            stringPart = MultipartBody.Part.createFormData("location", serializer.toJson(null))
            builder = builder.addPart(stringPart)

            stringPart = MultipartBody.Part.createFormData("meeting_started", serializer.toJson(request.isMeetingStarted?.toString()))
            builder = builder.addPart(stringPart)

            stringPart = MultipartBody.Part.createFormData("invite_to_a_meeting", serializer.toJson(request.hasInviteToMeeting?.toString()))
            builder = builder.addPart(stringPart)

            stringPart = MultipartBody.Part.createFormData("age", serializer.toJson(request.age?.toString()))
            builder = builder.addPart(stringPart)

            stringPart = MultipartBody.Part.createFormData("bio", serializer.toJson(request.bio))
            builder = builder.addPart(stringPart)

            stringPart = MultipartBody.Part.createFormData("description", serializer.toJson(request.description))
            builder = builder.addPart(stringPart)

            if(updateImage) {
                var filePart = if(file != null) {
                    MultipartBody.Part.createFormData(
                        "image", file.name, RequestBody.create(
                            MediaType.parse("image/*"), file
                        )
                    )

                } else {
                    MultipartBody.Part.createFormData("image", serializer.toJson(null))
                }
                builder = builder.addPart(filePart)
            }

            api.updateProfileAsync(builder.build().parts())
        }, ErrorsListResponse::class.java)
    }
}