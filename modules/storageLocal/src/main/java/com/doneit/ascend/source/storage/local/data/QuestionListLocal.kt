package com.doneit.ascend.source.storage.local.data

import androidx.room.*
import kotlin.random.Random

@Entity(tableName = "questions")
data class QuestionListLocal(
    @PrimaryKey
    @ColumnInfo(name = "question_id")
    var id: Long = Random.nextLong(),

    @Ignore
    var community: CommunityLocal? = null,

    @Ignore
    var questionItems: List<QuestionItemLocal> = listOf()
)