package com.doneit.ascend.domain.use_case.interactor.user

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.RateEntity
import com.doneit.ascend.domain.entity.common.BaseCallback
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.entity.user.AuthEntity
import com.doneit.ascend.domain.entity.user.UserEntity
import kotlinx.coroutines.CoroutineScope

interface UserUseCase {

    suspend fun signIn(logInDTO: LogInUserDTO): ResponseEntity<AuthEntity, List<String>>

    suspend fun signOut(): ResponseEntity<Unit, List<String>>

    fun removeAccounts()

    suspend fun socialSignIn(socialLogInDTO: SocialLogInDTO): ResponseEntity<AuthEntity, List<String>>

    suspend fun signUp(registerDTO: SignUpDTO): ResponseEntity<AuthEntity, List<String>>

    suspend fun deleteAccount(): ResponseEntity<Unit, List<String>>

    suspend fun signUpValidation(registerDTO: SignUpDTO): ResponseEntity<Unit, List<String>>

    suspend fun getConfirmationCode(phone: String): ResponseEntity<Unit, List<String>>

    suspend fun forgotPassword(phone: String): ResponseEntity<Unit, List<String>>

    suspend fun resetPassword(resetDTO: ResetPasswordDTO) : ResponseEntity<Unit, List<String>>

    suspend fun changePassword(dto: ChangePasswordDTO): ResponseEntity<Unit, List<String>>

    suspend fun update(user: UserEntity)

    suspend fun hasSignedInUser(): Boolean

    fun getUserLive(): LiveData<UserEntity?>

    suspend fun getUser() : UserEntity?

    suspend fun report(content: String, id: String): ResponseEntity<Unit, List<String>>

    suspend fun updateCurrentUserData()

    suspend fun updateProfile(request: UpdateProfileDTO): ResponseEntity<UserEntity, List<String>>

    suspend fun getRates(model: RatingsDTO): PagedList<RateEntity>

    suspend fun changePhone(dto: ChangePhoneDTO): ResponseEntity<Unit, List<String>>

    suspend fun changeEmail(dto: ChangeEmailDTO): ResponseEntity<Unit, List<String>>

    suspend fun updateFirebase(firebaseId: String): ResponseEntity<Unit, List<String>>

    fun shareUser(
        coroutineScope: CoroutineScope,
        userId: Long,
        shareDTO: ShareDTO,
        baseCallback: BaseCallback<Unit>
    )
}