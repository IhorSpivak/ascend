package com.doneit.ascend.domain.use_case.interactor.user

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.common.RequestEntity

interface UserUseCase {
    suspend fun signIn(logInModel: LogInUserModel): RequestEntity<AuthEntity, List<String>>

    suspend fun socialSignIn(socialLogInModel: SocialLogInModel): RequestEntity<AuthEntity, List<String>>

    suspend fun signUp(registerModel: SignUpModel): RequestEntity<AuthEntity, List<String>>

    suspend fun signUpValidation(registerModel: SignUpModel): RequestEntity<Unit, List<String>>

    suspend fun getConfirmationCode(phone: String): RequestEntity<Unit, List<String>>

    suspend fun forgotPassword(phone: String): RequestEntity<Unit, List<String>>

    suspend fun resetPassword(resetModel: ResetPasswordModel) : RequestEntity<Unit, List<String>>

    suspend fun insert(user: UserEntity, token: String)

    fun getUser(): LiveData<UserEntity>
}