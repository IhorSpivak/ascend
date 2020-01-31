package com.doneit.ascend.domain.gateway.gateway.base

import androidx.annotation.VisibleForTesting
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse
import com.vrgsoft.core.gateway.IBaseGateway
import com.vrgsoft.networkmanager.Error
import com.vrgsoft.networkmanager.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseGateway(override val networkManager: NetworkManager) : IBaseGateway {

    @VisibleForTesting(otherwise = VisibleForTesting.PROTECTED)
    suspend fun <T, E> executeRemote(call: suspend (() -> RemoteResponse<T, E>)): RemoteResponse<T, E> {
        networkManager.startProcessing()

        val result = withContext(Dispatchers.IO) {
            call.invoke()
        }

        networkManager.stopProcessing()

        if (result.errorModel != null) {
            val message = calculateMessage(result.errorModel)

            networkManager.errors.call(Error(message))
        }

        return result
    }

    open fun <T> calculateMessage(error: T): String {
        return ""//todo, not required for now
    }
}