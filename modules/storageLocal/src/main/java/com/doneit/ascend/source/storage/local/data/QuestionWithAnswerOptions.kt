package com.doneit.ascend.source.storage.local.data

import androidx.room.Embedded
import androidx.room.Relation

data class QuestionWithAnswerOptions(
    @Embedded
    val question: QuestionLocal,

    @Relation(
        parentColumn = "question_id",
        entity = AnswerOptionLocal::class,
        entityColumn = "owner_id"
    )
    val answerOptions: List<AnswerOptionLocal>? = null
)