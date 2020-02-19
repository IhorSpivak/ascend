package com.doneit.ascend.domain.gateway.common.mapper.to_locale

import com.doneit.ascend.domain.entity.AnswerOptionEntity
import com.doneit.ascend.domain.entity.CommunityQuestionEntity
import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.domain.entity.QuestionListEntity
import com.doneit.ascend.source.storage.local.data.CommunityLocal
import com.doneit.ascend.source.storage.local.data.first_time_login.AnswerOptionLocal
import com.doneit.ascend.source.storage.local.data.first_time_login.QuestionItemLocal
import com.doneit.ascend.source.storage.local.data.first_time_login.QuestionListLocal

fun AnswerOptionEntity.toLocal(): AnswerOptionLocal {
    return AnswerOptionLocal(
        id,
        title
    )
}

fun List<String>.toAnswerOptionsLocal(): List<AnswerOptionLocal> {
    return this.map {
        AnswerOptionLocal(
            title = it
        )
    }
}

fun CommunityQuestionEntity.toLocal(): CommunityLocal {
    val model =  CommunityLocal(
        title = this@toLocal.title,
        options = this@toLocal.answerOptions.toAnswerOptionsLocal()
    )

    model.options.forEach {
        it.communityId = model.id
    }

    return model
}

fun QuestionEntity.toLocal(): QuestionItemLocal {
    return QuestionItemLocal(
        id = this@toLocal.id,
        title = this@toLocal.title,
        createdAt = this@toLocal.createdAt,
        updatedAt = this@toLocal.updatedAt,
        type = this@toLocal.type
    )
}

fun QuestionListEntity.toLocal(): QuestionListLocal {
    val model = QuestionListLocal(
        questionItems = questions.map { it.toLocal() },
        community = community?.toLocal()
    )

    model.questionItems.forEach {
        it.questionId = model.id
    }

    model.community?.questionId = model.id

    return model
}