package com.vrgsoft.carButler.domain.gateway.gateway

import androidx.lifecycle.liveData
import com.vrgsoft.carButler.domain.entity.LoginUserModel
import com.vrgsoft.carButler.domain.entity.User
import com.vrgsoft.carButler.domain.gateway.common.mapper.entities.toLoginRequest
import com.vrgsoft.carButler.domain.gateway.common.mapper.remote.toEntity
import com.vrgsoft.carButler.domain.use_case.gateway.IUserGateway
import com.vrgsoft.core.gateway.BaseGateway
import com.vrgsoft.core.remote.error.BaseError
import com.vrgsoft.core.remote.mapDataIfSuccess
import com.vrgsoft.networkmanager.NetworkManager
import com.vrgsoft.carButler.source.storage.remote.repository.sample.IUserRepository as RemoteAccount

internal class UserGateway(
    errors: NetworkManager,
    private val remote: RemoteAccount
) : BaseGateway(errors), IUserGateway {

    override fun calculateMessage(error: BaseError): String {
        return ""//todo
    }

    override fun login(loginModel: LoginUserModel)= liveData<User> {
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