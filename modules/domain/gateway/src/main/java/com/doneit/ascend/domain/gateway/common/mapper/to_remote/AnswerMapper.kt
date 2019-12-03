package com.doneit.ascend.domain.gateway.common.mapper.to_remote

import com.doneit.ascend.domain.entity.AnswerEntity
import com.doneit.ascend.source.storage.remote.data.request.AnswerItemRequest
import com.doneit.ascend.source.storage.remote.data.request.AnswerRequest

fun AnswerEntity.toRequest(): AnswerItemRequest {
    return AnswerItemRequest(
        questionId,
        answer,
        answerOptionId
    )
}

fun List<AnswerEntity>.toRequest(): AnswerRequest {
    return AnswerRequest(this.map {
        it.toRequest()
    })
}