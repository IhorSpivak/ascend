package com.doneit.ascend.domain.gateway.gateway

import com.doneit.ascend.domain.entity.LoginUserModel
import com.doneit.ascend.domain.entity.RequestEntity
import com.doneit.ascend.domain.entity.User
import com.doneit.ascend.domain.gateway.common.mapper.toRequestEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toLoginRequest
import com.doneit.ascend.domain.gateway.gateway.base.BaseGateway
import com.doneit.ascend.domain.use_case.gateway.IUserGateway
import com.doneit.ascend.source.storage.remote.repository.IUserRepository
import com.vrgsoft.networkmanager.NetworkManager

internal class UserGateway(
    errors: NetworkManager,
    private val remote: IUserRepository
) : BaseGateway(errors), IUserGateway {

    override fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }

    override suspend fun login(loginModel: LoginUserModel): RequestEntity<User, List<String>> {
        return executeRemote { remote.login(loginModel.toLoginRequest()) }.toRequestEntity(
            {
                it?.toEntity()
            },
            {
                it?.errors
            }
        )
    }
}