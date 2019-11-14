package com.doneit.ascend.domain.gateway.di

import com.vrgsoft.networkmanager.NetworkManager
import com.doneit.ascend.domain.gateway.gateway.UserGateway
import com.doneit.ascend.domain.use_case.gateway.IUserGateway
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object GatewayModule {
    fun get() = Kodein.Module("GatewayModule") {
        bind() from singleton { NetworkManager() }

        bind<IUserGateway>() with provider {
            UserGateway(
                instance(),
                instance()
            )
        }
    }
}