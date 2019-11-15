package com.doneit.ascend.domain.gateway.gateway

import com.doneit.ascend.domain.entity.LoginUserModel
import com.doneit.ascend.domain.entity.User
import com.doneit.ascend.domain.gateway.common.mapper.entities.toLoginRequest
import com.doneit.ascend.domain.gateway.common.mapper.remote.toEntity
import com.doneit.ascend.domain.use_case.gateway.IUserGateway
import com.vrgsoft.core.gateway.BaseGateway
import com.vrgsoft.core.remote.error.BaseError
import com.vrgsoft.core.remote.mapDataIfSuccess
import com.vrgsoft.networkmanager.NetworkManager
import com.doneit.ascend.source.storage.remote.repository.sample.IUserRepository as RemoteAccount

internal class UserGateway(
    errors: NetworkManager,
    private val remote: RemoteAccount
) : BaseGateway(errors), IUserGateway {

    override fun calculateMessage(error: BaseError): String {
        return ""//todo
    }

    override suspend fun login(loginModel: LoginUserModel): User? {
        return executeRemote { remote.login(loginModel.toLoginRequest()) }
            .mapDataIfSuccess {
                it.toEntity()
            }
    }
}