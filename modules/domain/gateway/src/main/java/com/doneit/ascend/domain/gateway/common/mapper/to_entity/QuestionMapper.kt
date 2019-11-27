package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.AnswerOptionEntity
import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.source.storage.local.data.AnswerOptionLocal
import com.doneit.ascend.source.storage.local.data.QuestionLocal
import com.doneit.ascend.source.storage.local.data.QuestionWithAnswerOptions
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

fun AnswerOptionEntity.toEntity(questionId: Long): AnswerOptionLocal {
    return AnswerOptionLocal(
        id,
        title,
        position,
        createdAt,
        updatedAt,
        questionId
    )
}

fun List<AnswerOptionEntity>.toAnswerEntityList(id: Long): List<AnswerOptionLocal> {
    return this.map {
        it.toEntity(id)
    }
}

fun QuestionEntity.toEntity(): QuestionLocal {
    return QuestionLocal(
        id,
        title,
        type,
        createdAt,
        updatedAt,
        options?.toAnswerEntityList(id) ?: listOf()
    )
}

fun List<QuestionEntity>.toQuestionEntityList(): List<QuestionLocal> {
    return this.map {
        it.toEntity()
    }
}

fun AnswerOptionLocal.toEntity(): AnswerOptionEntity {
    return AnswerOptionEntity(
        id,
        title,
        position,
        createdAt,
        updatedAt
    )
}

fun List<AnswerOptionLocal>.toAnswerOptionList(): List<AnswerOptionEntity> {
    return this.map {
        it.toEntity()
    }
}

fun QuestionWithAnswerOptions.toEntity(): QuestionEntity {
    return QuestionEntity(
        id = this@toEntity.question.id,
        title = this@toEntity.question.title,
        type = this@toEntity.question.type,
        createdAt = this@toEntity.question.createdAt,
        updatedAt = this@toEntity.question.updatedAt,
        options = this@toEntity.answerOptions?.toAnswerOptionList()
    )
}

fun List<QuestionWithAnswerOptions>.toQuestionList(): List<QuestionEntity> {
    return this.map {
        it.toEntity()
    }
}