package com.vrgsoft.carButler.domain.gateway.di

import com.vrgsoft.networkmanager.NetworkManager
import com.vrgsoft.carButler.domain.gateway.gateway.SampleGateway
import com.vrgsoft.carButler.domain.use_case.gateway.ISampleGateway
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

object GatewayModule {
    fun get() = Kodein.Module("GatewayModule") {
        bind() from singleton { NetworkManager() }

        bind<ISampleGateway>() with provider {
            SampleGateway(
                instance(),
                instance()
            )
        }
    }
}