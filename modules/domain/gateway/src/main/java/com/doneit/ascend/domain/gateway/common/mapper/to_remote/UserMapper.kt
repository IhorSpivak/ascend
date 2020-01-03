package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.source.storage.remote.data.request.*

fun LogInUserModel.toLoginRequest(): LogInRequest {
    return LogInRequest(
        number,
        password
    )
}

fun SocialLogInModel.toSocialLoginRequest() : SocialLoginRequest {
    return SocialLoginRequest(
        socialType,
        accessToken,
        accessTokenSecret
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

fun UpdateProfileModel.toRequest(): UpdateProfileRequest {
    return UpdateProfileRequest(
        fullName,
        displayName,
        location,
        isMeetingStarted,
        hasNewGroups,
        hasInviteToMeeting,
        age,
        bio,
        description
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