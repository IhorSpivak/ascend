package com.doneit.ascend.domain.gateway.common.mapper

import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.source.storage.remote.data.response.common.RemoteResponse

fun <T, E, R, F> RemoteResponse<T, E>.toResponseEntity(successMapper: (T?) -> R?, errorMapper: (E?) -> F?): ResponseEntity<R, F> {
    return ResponseEntity(
        isSuccessful,
        successMapper.invoke(successModel),
        errorMapper.invoke(errorModel)
    )
}