package com.doneit.ascend.domain.gateway.gateway

import androidx.lifecycle.liveData
import com.doneit.ascend.domain.gateway.common.mapper.entities.toLoginRequest
import com.doneit.ascend.domain.gateway.common.mapper.remote.toEntity
import com.doneit.ascend.domain.use_case.gateway.IUserGateway
import com.vrgsoft.core.gateway.BaseGateway
import com.vrgsoft.core.remote.error.BaseError
import com.vrgsoft.networkmanager.NetworkManager
import com.doneit.ascend.source.storage.remote.repository.sample.IUserRepository as RemoteAccount

internal class UserGateway(
    errors: NetworkManager,
    private val remote: RemoteAccount
) : BaseGateway(errors), IUserGateway {

    override fun calculateMessage(error: BaseError): String {
        return ""//todo
    }

    override fun login(loginModel: com.doneit.ascend.domain.entity.LoginUserModel)= liveData<com.doneit.ascend.domain.entity.User> {
        try {
            val result = executeRemote { remote.login(loginModel.toLoginRequest()) }
                .mapDataIfSuccess {
                    it.toEntity()
                }
            if(result != null){
                emit(result)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}