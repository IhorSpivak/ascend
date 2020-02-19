package com.doneit.ascend.source.storage.local.data.first_time_login

import androidx.room.Embedded
import androidx.room.Relation
import com.doneit.ascend.source.storage.local.data.CommunityLocal

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