package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.CreateCardDTO
import com.doneit.ascend.source.storage.remote.data.request.CreateCardRequest

fun CreateCardDTO.toRequest(): CreateCardRequest {
    return CreateCardRequest(
        name,
        token
    )
}