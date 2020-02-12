package com.doneit.ascend.domain.use_case.gateway

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AuthEntity
import com.doneit.ascend.domain.entity.RateEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*

interface IUserGateway {
    suspend fun signIn(loginMode: LogInUserDTO): ResponseEntity<AuthEntity, List<String>>

    suspend fun signOut(): ResponseEntity<Unit, List<String>>

    suspend fun socialSignIn(socialLoginDTO: SocialLogInDTO): ResponseEntity<AuthEntity, List<String>>

    suspend fun signUp(signUpDTO: SignUpDTO): ResponseEntity<AuthEntity, List<String>>

    suspend fun deleteAccount(): ResponseEntity<Unit, List<String>>

    suspend fun signUpValidation(signUpDTO: SignUpDTO): ResponseEntity<Unit, List<String>>

    suspend fun getConfirmationCode(phone: String): ResponseEntity<Unit, List<String>>

    suspend fun forgotPassword(phone: String): ResponseEntity<Unit, List<String>>

    suspend fun changePassword(dto: ChangePasswordDTO): ResponseEntity<Unit, List<String>>

    suspend fun resetPassword(resetDTO: ResetPasswordDTO): ResponseEntity<Unit, List<String>>

    suspend fun update(user: UserEntity)

    suspend fun hasSignedInUser(): Boolean

    fun getUserLive(): LiveData<UserEntity?>

    suspend fun getUser(): UserEntity?

    suspend fun report(content: String, id: Long): ResponseEntity<Unit, List<String>>

    suspend fun getProfile() : ResponseEntity<UserEntity, List<String>>

    suspend fun updateProfile(groupDTO: UpdateProfileDTO): ResponseEntity<UserEntity, List<String>>

    suspend fun getRating(ratingsModel: RatingsDTO): PagedList<RateEntity>

    suspend fun changePhone(dto: ChangePhoneDTO): ResponseEntity<Unit, List<String>>

    suspend fun changeEmail(dto: ChangeEmailDTO): ResponseEntity<Unit, List<String>>

    suspend fun updateFirebase(firebaseId: String): ResponseEntity<Unit, List<String>>
}