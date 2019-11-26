package com.doneit.ascend.domain.gateway.gateway

import com.doneit.ascend.domain.entity.AuthEntity
import com.doneit.ascend.domain.entity.LogInUserModel
import com.doneit.ascend.domain.entity.ResetPasswordModel
import com.doneit.ascend.domain.entity.SignUpModel
import com.doneit.ascend.domain.entity.common.RequestEntity
import com.doneit.ascend.domain.gateway.common.mapper.toRequestEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toLoginRequest
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toResetPasswordRequest
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toSignUpRequest
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.use_case.gateway.IUserGateway
import com.doneit.ascend.source.storage.remote.data.request.PhoneRequest
import com.doneit.ascend.source.storage.remote.repository.user.IUserRepository
import com.vrgsoft.networkmanager.NetworkManager

internal class UserGateway(
    errors: NetworkManager,
    private val remote: IUserRepository
) : BaseGateway(errors), IUserGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun signIn(logInModel: LogInUserModel): RequestEntity<AuthEntity, List<String>> {
        return executeRemote { remote.signIn(logInModel.toLoginRequest()) }.toRequestEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun signUp(signUpModel: SignUpModel): RequestEntity<AuthEntity, List<String>> {
        return executeRemote { remote.signUp(signUpModel.toSignUpRequest()) }.toRequestEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun getConfirmationCode(phone: String): RequestEntity<Unit, List<String>> {
        return executeRemote { remote.getConfirmationCode(PhoneRequest(phone)) }.toRequestEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun forgotPassword(phone: String): RequestEntity<Unit, List<String>> {
        return executeRemote { remote.forgotPassword(PhoneRequest(phone)) }.toRequestEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }

    override suspend fun resetPassword(resetModel: ResetPasswordModel): RequestEntity<Unit, List<String>> {
        return executeRemote { remote.resetPassword(resetModel.toResetPasswordRequest()) }.toRequestEntity(
            {
                Unit
            },
            {
                it?.errors
            }
        )
    }
}