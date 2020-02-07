package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.source.storage.remote.data.request.*

fun LogInUserModel.toLoginRequest(firebaseId: String): LogInRequest {
    return LogInRequest(
        number,
        password,
        firebaseId
    )
}

fun SocialLogInModel.toSocialLoginRequest(firebaseId: String): SocialLoginRequest {
    return SocialLoginRequest(
        socialType,
        accessToken,
        accessTokenSecret,
        firebaseId
    )
}

fun SignUpModel.toSignUpRequest(): SignUpRequest {
    return SignUpRequest(
        email,
        phone,
        name,
        password,
        passwordConfirmation,
        code
    )
}

fun ResetPasswordModel.toResetPasswordRequest(): ResetPasswordRequest {
    return ResetPasswordRequest(
        phone,
        code,
        password,
        passwordConfirmation
    )
}

fun ChangePasswordModel.toRequest(): ChangePasswordRequest {
    return ChangePasswordRequest(
        currentPassword,
        password,
        passwordConfirmation
    )
}

fun UpdateProfileModel.toRequest(): UpdateProfileRequest {
    return UpdateProfileRequest(
        fullName,
        displayName,
        location,
        isMeetingStarted,
        hasNewGroups,
        hasInviteToMeeting,
        birthday?.toRemoteStringShort(),
        bio,
        description,
        removeImage
    )
}

fun RatingsModel.toRequest(currPage: Int): RateRequest {
    return RateRequest(
        currPage,
        perPage,
        sortColumn,
        sortType?.toString(),
        fullName,
        createdAtFrom?.toRemoteString(),
        createdAtTo?.toRemoteString(),
        updatedAtFrom?.toRemoteString(),
        updatedAtTo?.toRemoteString()
    )
}

fun ChangePhoneModel.toRequest(): ChangePhoneRequest {
    return ChangePhoneRequest(
        password,
        phoneNumber,
        code
    )
}

fun ChangeEmailModel.toRequest(): ChangeEmailRequest {
    return ChangeEmailRequest(
        email,
        password
    )
}