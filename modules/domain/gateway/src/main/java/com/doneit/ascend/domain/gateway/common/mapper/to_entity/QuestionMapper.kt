package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.AnswerOptionEntity
import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.source.storage.remote.data.response.AnswerOptionResponse
import com.doneit.ascend.source.storage.remote.data.response.QuestionResponse

fun AnswerOptionResponse.toEntity(): AnswerOptionEntity {
    return AnswerOptionEntity(
        id,
        title,
        position,
        createdAt,
        updatedAt
    )
}

fun List<AnswerOptionResponse>.toAnswersEntityList(): List<AnswerOptionEntity> {
    return this.map {
        it.toEntity()
    }
}

fun QuestionResponse.toEntity(): QuestionEntity {
    return QuestionEntity(
        id,
        title,
        type,
        createdAt,
        updatedAt,
        options?.toAnswersEntityList()
    )
}

fun List<QuestionResponse>.toEntityList(): List<QuestionEntity> {
    return this.map {
        it.toEntity()
    }
}