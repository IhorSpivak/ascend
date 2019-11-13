package com.vrgsoft.carButler.domain.gateway.gateway

import com.vrgsoft.carButler.domain.use_case.gateway.ISampleGateway
import com.vrgsoft.core.gateway.BaseGateway
import com.vrgsoft.core.remote.error.BaseError
import com.vrgsoft.networkmanager.NetworkManager
import com.vrgsoft.carButler.source.storage.remote.repository.sample.ISampleRepository as RemoteAccount

internal class SampleGateway(
    errors: NetworkManager,
    private val remote: RemoteAccount
) : BaseGateway(errors), ISampleGateway {
    override fun calculateMessage(error: BaseError): String {
        return ""//todo
    }
}