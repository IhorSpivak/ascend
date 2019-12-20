package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.AnswerOptionEntity
import com.doneit.ascend.domain.entity.CommunityQuestionEntity
import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.domain.entity.QuestionListEntity
import com.doneit.ascend.source.storage.local.data.*
import com.doneit.ascend.source.storage.remote.data.response.AnswerOptionResponse
import com.doneit.ascend.source.storage.remote.data.response.CommunityResponse
import com.doneit.ascend.source.storage.remote.data.response.QuestionResponse
import com.doneit.ascend.source.storage.remote.data.response.QuestionsListResponse

fun AnswerOptionResponse.toEntity(): AnswerOptionEntity {
    return AnswerOptionEntity(
        id,
        title
    )
}

fun QuestionResponse.toEntity(): QuestionEntity {
    return QuestionEntity(
        id,
        title,
        type,
        createdAt,
        updatedAt
    )
}

fun CommunityResponse.toEntity(): CommunityQuestionEntity {
    return CommunityQuestionEntity(
        title,
        answerOptions
    )
}

fun QuestionsListResponse.toEntityList(): QuestionListEntity {
    return QuestionListEntity(
        questions = this.questions.map {
            it.toEntity()
        },
        community = this.community.toEntity()
    )
}

fun AnswerOptionEntity.toEntity(): AnswerOptionLocal {
    return AnswerOptionLocal(
        id,
        title
    )
}

fun List<AnswerOptionEntity>.toAnswerEntityList(): List<AnswerOptionLocal> {
    return this.map {
        it.toEntity()
    }
}

fun QuestionEntity.toEntity(): QuestionItemLocal {
    return QuestionItemLocal(
        id = this@toEntity.id,
        title = this@toEntity.title,
        createdAt = this@toEntity.createdAt,
        updatedAt = this@toEntity.updatedAt,
        type = this@toEntity.type
    )
}

fun List<String>.toAnswerOptionsEntity(): List<AnswerOptionLocal> {
    return this.map {
        AnswerOptionLocal(
            title = it
        )
    }
}

fun CommunityQuestionEntity.toEntity(): CommunityLocal {
    return CommunityLocal(
        title = this@toEntity.title,
        options = this@toEntity.answerOptions.toAnswerOptionsEntity()
    )
}

fun List<QuestionEntity>.toListEntity(): List<QuestionItemLocal> {
    return this.map {
        it.toEntity()
    }
}

fun QuestionListEntity.toQuestionEntityList(): QuestionListLocal {
    return QuestionListLocal(
        questionItems = questions.toListEntity(),
        community = community?.toEntity()
    )
}

fun AnswerOptionLocal.toEntity(): AnswerOptionEntity {
    return AnswerOptionEntity(
        id,
        title
    )
}

fun List<AnswerOptionLocal>.toAnswerOptionList(): List<AnswerOptionEntity> {
    return this.map {
        it.toEntity()
    }
}

fun QuestionItemLocal.toEntity(): QuestionEntity {
    return QuestionEntity(
        id = this@toEntity.id,
        title = this@toEntity.title,
        type = this@toEntity.type,
        createdAt = this@toEntity.createdAt,
        updatedAt = this@toEntity.updatedAt
    )
}

fun List<QuestionItemLocal>.toQuestionItemsList(): List<QuestionEntity> {
    return this.map {
        it.toEntity()
    }
}

fun List<AnswerOptionLocal>.toEntityList(): List<String> {
    return this.map {
        it.title
    }
}

fun CommunityLocal.toEntity(): CommunityQuestionEntity {
    return CommunityQuestionEntity(
        title,
        options.toEntityList()
    )
}

fun QuestionWithAnswerOptions.toEntity(): QuestionListEntity {
    return QuestionListEntity(
        questions = this@toEntity.questionItem?.toQuestionItemsList() ?: listOf(),
        community = this@toEntity.community?.toEntity()
    )
}