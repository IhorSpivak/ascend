package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.dto.CreateCardModel
import com.doneit.ascend.source.storage.remote.data.request.CreateCardRequest

fun CreateCardModel.toRequest(): CreateCardRequest {
    return CreateCardRequest(
        name,
        token
    )
}