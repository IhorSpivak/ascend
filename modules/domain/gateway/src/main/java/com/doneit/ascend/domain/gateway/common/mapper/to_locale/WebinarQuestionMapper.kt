package com.doneit.ascend.domain.gateway.common.mapper.to_locale

import com.doneit.ascend.domain.entity.webinar_question.WebinarQuestionEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRemoteString
import com.doneit.ascend.source.storage.local.data.WebinarQuestionLocal

fun WebinarQuestionEntity.toLocal(): WebinarQuestionLocal {
    return WebinarQuestionLocal(
        id,
        content,
        createdAt?.toRemoteString(),
        updatedAt?.toRemoteString(),
        userId,
        fullName,
        image?.toLocal()
    )
}
