package com.doneit.ascend.source.storage.local.data

import androidx.room.Embedded
import androidx.room.Relation

data class QuestionWithAnswerOptions(

    @Embedded
    val question: QuestionListLocal,

    @Relation(
        parentColumn = "question_id",
        entity = QuestionItemLocal::class,
        entityColumn = "question_id"
    )
    val questionItem: List<QuestionItemLocal>? = null,

    @Relation(
        parentColumn = "question_id",
        entity = CommunityLocal::class,
        entityColumn = "question_id"
    )
    val community: CommunityLocal? = null
)