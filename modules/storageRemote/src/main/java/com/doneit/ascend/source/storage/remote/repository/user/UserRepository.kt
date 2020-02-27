package com.doneit.ascend.source.storage.remote.repository.user

import com.doneit.ascend.source.storage.remote.api.UserApi
import com.doneit.ascend.source.storage.remote.data.request.*
import com.doneit.ascend.source.storage.remote.data.response.OKResponse
import com.doneit.ascend.source.storage.remote.data.response.RatesResponse
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.doneit.ascend.source.storage.remote.data.response.errors.ErrorsListResponse
import com.doneit.ascend.source.storage.remote.data.response.user.AuthResponse
import com.doneit.ascend.source.storage.remote.data.response.user.ProfileResponse
import com.doneit.ascend.source.storage.remote.data.response.user.UpdateProfileResponse
import com.doneit.ascend.source.storage.remote.repository.base.BaseRepository
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
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

    override suspend fun changePassword(request: ChangePasswordRequest): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.changePasswordAsync(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun resetPassword(request: ResetPasswordRequest): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.resetPassword(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun report(
        content: String,
        id: String
    ): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.report(id, content) }, ErrorsListResponse::class.java)
    }

    override suspend fun getProfile(): RemoteResponse<ProfileResponse, ErrorsListResponse> {
        return execute({ api.getProfileAsync() }, ErrorsListResponse::class.java)
    }

    override suspend fun updateProfile(
        file: File?,
        request: UpdateProfileRequest
    ): RemoteResponse<UpdateProfileResponse, ErrorsListResponse> {
        return execute({

            var builder = MultipartBody.Builder()

            var stringPart: MultipartBody.Part
            request.fullName?.let {
                stringPart = MultipartBody.Part.createFormData("full_name", request.fullName)
                builder = builder.addPart(stringPart)
            }

            request.displayName?.let {
                stringPart = MultipartBody.Part.createFormData("display_name", request.displayName)
                builder = builder.addPart(stringPart)
            }

            request.location?.let {
                stringPart = MultipartBody.Part.createFormData("location", request.location)
                builder = builder.addPart(stringPart)
            }

            request.isMeetingStarted?.let {
                stringPart = MultipartBody.Part.createFormData(
                    "meeting_started",
                    gson.toJson(request.isMeetingStarted)
                )
                builder = builder.addPart(stringPart)
            }

            request.hasNewGroups?.let {
                stringPart = MultipartBody.Part.createFormData(
                    "new_groups",
                    gson.toJson(request.hasNewGroups)
                )
                builder = builder.addPart(stringPart)
            }

            request.hasInviteToMeeting?.let {
                stringPart = MultipartBody.Part.createFormData(
                    "invite_to_a_meeting",
                    gson.toJson(request.hasInviteToMeeting)
                )
                builder = builder.addPart(stringPart)
            }

            request.birthday?.let {
                stringPart = MultipartBody.Part.createFormData("birthday", gson.toJson(request.birthday))
                builder = builder.addPart(stringPart)
            }

            request.bio?.let {
                stringPart = MultipartBody.Part.createFormData("bio", request.bio)
                builder = builder.addPart(stringPart)
            }

            request.description?.let {
                stringPart = MultipartBody.Part.createFormData("description", request.description)
                builder = builder.addPart(stringPart)
            }

            request.removeImage?.let {
                stringPart = MultipartBody.Part.createFormData("remove_image", gson.toJson(request.removeImage))
                builder = builder.addPart(stringPart)
            }

            file?.let {file ->
                val filePart = MultipartBody.Part.createFormData(
                "image", file.name, RequestBody.create(
                "image/*".toMediaTypeOrNull(), file
                )
                )
                builder = builder.addPart(filePart)
            }

            api.updateProfileAsync(builder.build().parts)
        }, ErrorsListResponse::class.java)
    }

    override suspend fun getRates(request: RateRequest): RemoteResponse<RatesResponse, ErrorsListResponse> {
        return execute({
            api.getRatingsAsync(
                request.page,
                request.perPage,
                request.sortColumn,
                request.sortType,
                request.fullName,
                request.createdAtFrom,
                request.createdAtTo,
                request.updatedAtFrom,
                request.updatedAtTo
            )
        }, ErrorsListResponse::class.java)
    }

    override suspend fun changePhone(request: ChangePhoneRequest): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.changePhone(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun changeEmail(request: ChangeEmailRequest): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.changeEmail(request) }, ErrorsListResponse::class.java)
    }

    override suspend fun updateFirebase(firebaseId: String): RemoteResponse<OKResponse, ErrorsListResponse> {
        return execute({ api.updateFirebase(firebaseId)}, ErrorsListResponse::class.java)
    }
}