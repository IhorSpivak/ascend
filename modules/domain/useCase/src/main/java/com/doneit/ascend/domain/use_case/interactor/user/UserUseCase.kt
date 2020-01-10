package com.doneit.ascend.domain.use_case.interactor.user

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AuthEntity
import com.doneit.ascend.domain.entity.RateEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*

interface UserUseCase {
    suspend fun signIn(logInModel: LogInUserModel): ResponseEntity<AuthEntity, List<String>>

    suspend fun signOut(): ResponseEntity<Unit, List<String>>

    suspend fun socialSignIn(socialLogInModel: SocialLogInModel): ResponseEntity<AuthEntity, List<String>>

    suspend fun signUp(registerModel: SignUpModel): ResponseEntity<AuthEntity, List<String>>

    suspend fun deleteAccount(): ResponseEntity<Unit, List<String>>

    suspend fun signUpValidation(registerModel: SignUpModel): ResponseEntity<Unit, List<String>>

    suspend fun getConfirmationCode(phone: String): ResponseEntity<Unit, List<String>>

    suspend fun forgotPassword(phone: String): ResponseEntity<Unit, List<String>>

    suspend fun resetPassword(resetModel: ResetPasswordModel) : ResponseEntity<Unit, List<String>>

    suspend fun changePassword(model: ChangePasswordModel): ResponseEntity<Unit, List<String>>

    suspend fun update(user: UserEntity)

    suspend fun hasSignedInUser(): Boolean

    fun getUserLive(): LiveData<UserEntity?>

    suspend fun getUser() : UserEntity?

    suspend fun report(content: String, id: Long): ResponseEntity<Unit, List<String>>

    suspend fun getProfile(): ResponseEntity<UserEntity, List<String>>

    suspend fun updateProfile(request: UpdateProfileModel): ResponseEntity<UserEntity, List<String>>

    suspend fun getRates(model: RatingsModel): PagedList<RateEntity>

    suspend fun changePhone(dto: ChangePhoneModel): ResponseEntity<Unit, List<String>>

    suspend fun changeEmail(model: ChangeEmailModel): ResponseEntity<Unit, List<String>>
}