package com.doneit.ascend.domain.use_case.interactor.user

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.*
import com.doneit.ascend.domain.entity.common.RequestEntity
import com.doneit.ascend.domain.use_case.gateway.IUserGateway

internal class UserInteractor(
    private val userGateway: IUserGateway
) : UserUseCase {
    override suspend fun signIn(logInModel: LogInUserModel): RequestEntity<AuthEntity, List<String>> {
        return userGateway.signIn(logInModel)
    }

    override suspend fun signOut(): RequestEntity<Unit, List<String>> {
        return userGateway.signOut()
    }

    override suspend fun socialSignIn(socialLogInModel: SocialLogInModel): RequestEntity<AuthEntity, List<String>> {
        return userGateway.socialSignIn(socialLogInModel)
    }

    override suspend fun signUp(registerModel: SignUpModel): RequestEntity<AuthEntity, List<String>> {
        return userGateway.signUp(registerModel)
    }

    override suspend fun deleteAccount(): RequestEntity<Unit, List<String>> {
        return userGateway.deleteAccount()
    }

    override suspend fun signUpValidation(registerModel: SignUpModel): RequestEntity<Unit, List<String>> {
        return userGateway.signUpValidation(registerModel)
    }

    override suspend fun getConfirmationCode(phone: String): RequestEntity<Unit, List<String>> {
        return userGateway.getConfirmationCode(phone)
    }

    override suspend fun forgotPassword(phone: String): RequestEntity<Unit, List<String>> {
        return userGateway.forgotPassword(phone)
    }

    override suspend fun resetPassword(resetModel: ResetPasswordModel): RequestEntity<Unit, List<String>> {
        return userGateway.resetPassword(resetModel)
    }

    override suspend fun insert(user: UserEntity, token: String) {
        userGateway.insert(user, token)
    }

    override fun getUser(): LiveData<UserEntity?> {
        return userGateway.getUser()
    }
}