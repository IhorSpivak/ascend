package com.doneit.ascend.domain.use_case.interactor.user

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.AuthEntity
import com.doneit.ascend.domain.entity.RateEntity
import com.doneit.ascend.domain.entity.UserEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.use_case.gateway.IUserGateway

internal class UserInteractor(
    private val userGateway: IUserGateway
) : UserUseCase {
    override suspend fun signIn(logInDTO: LogInUserDTO): ResponseEntity<AuthEntity, List<String>> {
        return userGateway.signIn(logInDTO)
    }

    override suspend fun signOut(): ResponseEntity<Unit, List<String>> {
        return userGateway.signOut()
    }

    override suspend fun socialSignIn(socialLogInDTO: SocialLogInDTO): ResponseEntity<AuthEntity, List<String>> {
        return userGateway.socialSignIn(socialLogInDTO)
    }

    override suspend fun signUp(registerDTO: SignUpDTO): ResponseEntity<AuthEntity, List<String>> {
        return userGateway.signUp(registerDTO)
    }

    override suspend fun deleteAccount(): ResponseEntity<Unit, List<String>> {
        return userGateway.deleteAccount()
    }

    override suspend fun signUpValidation(registerDTO: SignUpDTO): ResponseEntity<Unit, List<String>> {
        return userGateway.signUpValidation(registerDTO)
    }

    override suspend fun getConfirmationCode(phone: String): ResponseEntity<Unit, List<String>> {
        return userGateway.getConfirmationCode(phone)
    }

    override suspend fun forgotPassword(phone: String): ResponseEntity<Unit, List<String>> {
        return userGateway.forgotPassword(phone)
    }

    override suspend fun resetPassword(resetDTO: ResetPasswordDTO): ResponseEntity<Unit, List<String>> {
        return userGateway.resetPassword(resetDTO)
    }

    override suspend fun changePassword(dto: ChangePasswordDTO): ResponseEntity<Unit, List<String>> {
        return userGateway.changePassword(dto)
    }

    override suspend fun update(user: UserEntity) {
        userGateway.update(user)
    }

    override fun getUserLive(): LiveData<UserEntity?> {
        return userGateway.getUserLive()
    }

    override suspend fun getUser(): UserEntity? {
        return userGateway.getUser()
    }

    override suspend fun hasSignedInUser(): Boolean {
        return userGateway.hasSignedInUser()
    }

    override suspend fun report(content: String, id: String): ResponseEntity<Unit, List<String>> {
        return userGateway.report(content, id)
    }

    override suspend fun getProfile(): ResponseEntity<UserEntity, List<String>> {
        return userGateway.getProfile()
    }

    override suspend fun updateProfile(request: UpdateProfileDTO): ResponseEntity<UserEntity, List<String>> {
        return userGateway.updateProfile(request)
    }

    override suspend fun getRates(model: RatingsDTO): PagedList<RateEntity> {
        return userGateway.getRating(model)
    }

    override suspend fun changePhone(dto: ChangePhoneDTO): ResponseEntity<Unit, List<String>> {
        return userGateway.changePhone(dto)
    }

    override suspend fun changeEmail(dto: ChangeEmailDTO): ResponseEntity<Unit, List<String>> {
        return userGateway.changeEmail(dto)
    }

    override suspend fun updateFirebase(firebaseId: String): ResponseEntity<Unit, List<String>> {
        return userGateway.updateFirebase(firebaseId)
    }

}