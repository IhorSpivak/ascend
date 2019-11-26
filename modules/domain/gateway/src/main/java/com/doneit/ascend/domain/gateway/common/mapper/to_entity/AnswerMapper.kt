package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.AnswerEntity
import com.doneit.ascend.source.storage.remote.data.request.AnswerRequest

fun AnswerEntity.toEntity(): AnswerRequest {
    return AnswerRequest(
        questionId,
        answer,
        answerOptionId
    )
}

fun List<AnswerEntity>.toEntityList(): List<AnswerRequest> {
    return this.map {
        it.toEntity()
    }
}