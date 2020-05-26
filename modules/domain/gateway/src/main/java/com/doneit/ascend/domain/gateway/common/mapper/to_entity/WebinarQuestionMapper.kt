package com.doneit.ascend.domain.gateway.common.mapper.to_entity

import com.doneit.ascend.domain.entity.webinar_question.WebinarQuestionEntity
import com.doneit.ascend.source.storage.local.data.WebinarQuestionLocal
import com.doneit.ascend.source.storage.remote.data.response.group.WebinarQuestionResponse

fun WebinarQuestionResponse.toEntity(): WebinarQuestionEntity {
    return WebinarQuestionEntity(
        id,
        content,
        createdAt.toDate(),
        updatedAt.toDate(),
        userId,
        fullName,
        imageResponse?.toEntity()
    )
}

fun WebinarQuestionLocal.toEntity(): WebinarQuestionEntity {
    return WebinarQuestionEntity(
        id,
        content,
        createdAt?.toDate(),
        updatedAt?.toDate(),
        userId,
        fullName,
        image?.toEntity()
    )
}