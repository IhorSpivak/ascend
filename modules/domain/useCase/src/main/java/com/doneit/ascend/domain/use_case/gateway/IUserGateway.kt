package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.common.RequestEntity

interface IUserGateway {
    suspend fun signIn(loginMode: LogInUserModel): RequestEntity<AuthEntity, List<String>>

    suspend fun socialSignIn(socialLoginModel: SocialLogInModel): RequestEntity<AuthEntity, List<String>>

    suspend fun signUp(signUpModel: SignUpModel): RequestEntity<AuthEntity, List<String>>

    suspend fun signUpValidation(signUpModel: SignUpModel): RequestEntity<Unit, List<String>>

    suspend fun getConfirmationCode(phone: String): RequestEntity<Unit, List<String>>

    suspend fun forgotPassword(phone: String): RequestEntity<Unit, List<String>>

    suspend fun resetPassword(resetModel: ResetPasswordModel): RequestEntity<Unit, List<String>>
}