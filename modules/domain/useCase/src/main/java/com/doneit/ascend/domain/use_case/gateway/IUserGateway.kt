package com.doneit.ascend.domain.use_case.gateway

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*

interface IUserGateway {
    suspend fun signIn(loginMode: LogInUserModel): ResponseEntity<AuthEntity, List<String>>

    suspend fun signOut(): ResponseEntity<Unit, List<String>>

    suspend fun socialSignIn(socialLoginModel: SocialLogInModel): ResponseEntity<AuthEntity, List<String>>

    suspend fun signUp(signUpModel: SignUpModel): ResponseEntity<AuthEntity, List<String>>

    suspend fun deleteAccount(): ResponseEntity<Unit, List<String>>

    suspend fun signUpValidation(signUpModel: SignUpModel): ResponseEntity<Unit, List<String>>

    suspend fun getConfirmationCode(phone: String): ResponseEntity<Unit, List<String>>

    suspend fun forgotPassword(phone: String): ResponseEntity<Unit, List<String>>

    suspend fun changePassword(model: ChangePasswordModel): ResponseEntity<Unit, List<String>>

    suspend fun resetPassword(resetModel: ResetPasswordModel): ResponseEntity<Unit, List<String>>

    suspend fun update(user: UserEntity)

    suspend fun hasSignedInUser(): Boolean

    fun getUserLive(): LiveData<UserEntity?>

    suspend fun getUser(): UserEntity?

    suspend fun report(content: String, id: Long): ResponseEntity<Unit, List<String>>

    suspend fun getProfile() : ResponseEntity<UserEntity, List<String>>

    suspend fun updateProfile(groupModel: UpdateProfileModel): ResponseEntity<UserEntity, List<String>>

    suspend fun getRating(ratingsModel: RatingsModel): PagedList<RateEntity>

    suspend fun changePhone(model: ChangePhoneModel): ResponseEntity<Unit, List<String>>

    suspend fun changeEmail(model: ChangeEmailModel): ResponseEntity<Unit, List<String>>
}