package com.doneit.ascend.domain.gateway.common.mapper

import com.doneit.ascend.domain.entity.common.RequestEntity
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse

fun <T, E, R, F> RemoteResponse<T, E>.toRequestEntity(successMapper: (T?) -> R?, errorMapper: (E?) -> F?): RequestEntity<R, F> {
    return RequestEntity(
        isSuccessful,
        code,
        message,
        successMapper.invoke(successModel),
        errorMapper.invoke(errorModel)
    )
}