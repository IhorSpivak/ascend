package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.CommunityQuestionEntity
import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.domain.entity.QuestionListEntity
import com.doneit.ascend.domain.entity.user.AnswerOptionEntity
import com.doneit.ascend.source.storage.local.data.CommunityLocal
import com.doneit.ascend.source.storage.local.data.first_time_login.AnswerOptionLocal
import com.doneit.ascend.source.storage.local.data.first_time_login.QuestionItemLocal
import com.doneit.ascend.source.storage.local.data.first_time_login.QuestionListLocal
import com.doneit.ascend.source.storage.local.data.first_time_login.QuestionWithAnswerOptions
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

fun QuestionListLocal.toEntity(): QuestionListEntity {
    return QuestionListEntity(
        questions = this@toEntity.questionItems.toQuestionItemsList(),
        community = this@toEntity.community?.toEntity()
    )
}