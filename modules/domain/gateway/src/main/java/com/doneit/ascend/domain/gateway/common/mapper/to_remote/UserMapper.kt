package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.source.storage.remote.data.request.*

fun LogInUserDTO.toLoginRequest(firebaseId: String): LogInRequest {
    return LogInRequest(
        number,
        password,
        firebaseId
    )
}

fun SocialLogInDTO.toSocialLoginRequest(firebaseId: String): SocialLoginRequest {
    return SocialLoginRequest(
        socialType,
        accessToken,
        accessTokenSecret,
        firebaseId
    )
}

fun SignUpDTO.toSignUpRequest(): SignUpRequest {
    return SignUpRequest(
        email,
        phone,
        name,
        password,
        passwordConfirmation,
        code
    )
}

fun ResetPasswordDTO.toResetPasswordRequest(): ResetPasswordRequest {
    return ResetPasswordRequest(
        phone,
        code,
        password,
        passwordConfirmation
    )
}

fun ChangePasswordDTO.toRequest(): ChangePasswordRequest {
    return ChangePasswordRequest(
        currentPassword,
        password,
        passwordConfirmation
    )
}

fun UpdateProfileDTO.toRequest(): UpdateProfileRequest {
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

fun RatingsDTO.toRequest(currPage: Int): RateRequest {
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

fun ChangePhoneDTO.toRequest(): ChangePhoneRequest {
    return ChangePhoneRequest(
        password,
        phoneNumber,
        code
    )
}

fun ChangeEmailDTO.toRequest(): ChangeEmailRequest {
    return ChangeEmailRequest(
        email,
        password
    )
}