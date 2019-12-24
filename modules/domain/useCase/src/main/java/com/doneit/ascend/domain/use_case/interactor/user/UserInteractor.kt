package com.doneit.ascend.domain.use_case.interactor.user

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.*
import com.doneit.ascend.domain.use_case.gateway.IUserGateway

internal class UserInteractor(
    private val userGateway: IUserGateway
) : UserUseCase {
    override suspend fun signIn(logInModel: LogInUserModel): ResponseEntity<AuthEntity, List<String>> {
        return userGateway.signIn(logInModel)
    }

    override suspend fun signOut(): ResponseEntity<Unit, List<String>> {
        return userGateway.signOut()
    }

    override suspend fun socialSignIn(socialLogInModel: SocialLogInModel): ResponseEntity<AuthEntity, List<String>> {
        return userGateway.socialSignIn(socialLogInModel)
    }

    override suspend fun signUp(registerModel: SignUpModel): ResponseEntity<AuthEntity, List<String>> {
        return userGateway.signUp(registerModel)
    }

    override suspend fun deleteAccount(): ResponseEntity<Unit, List<String>> {
        return userGateway.deleteAccount()
    }

    override suspend fun signUpValidation(registerModel: SignUpModel): ResponseEntity<Unit, List<String>> {
        return userGateway.signUpValidation(registerModel)
    }

    override suspend fun getConfirmationCode(phone: String): ResponseEntity<Unit, List<String>> {
        return userGateway.getConfirmationCode(phone)
    }

    override suspend fun forgotPassword(phone: String): ResponseEntity<Unit, List<String>> {
        return userGateway.forgotPassword(phone)
    }

    override suspend fun resetPassword(resetModel: ResetPasswordModel): ResponseEntity<Unit, List<String>> {
        return userGateway.resetPassword(resetModel)
    }

    override suspend fun update(user: UserEntity) {
        userGateway.update(user)
    }

    override fun getUserLive(): LiveData<UserEntity?> {
        return userGateway.getUserLive()
    }

    override suspend fun getUser(): UserEntity? {
        return userGateway.geUser()
    }

    override suspend fun hasSignedInUser(): Boolean {
        return userGateway.hasSignedInUser()
    }

    override suspend fun report(content: String, id: Long): ResponseEntity<Unit, List<String>> {
        return userGateway.report(content,id)
    }

    override suspend fun getProfile(): ResponseEntity<ProfileEntity, List<String>> {
        return userGateway.getProfile()
    }

    override suspend fun updateProfile(request: UpdateProfileModel): ResponseEntity<ProfileEntity, List<String>> {
        return userGateway.updateProfile(request)
    }
}