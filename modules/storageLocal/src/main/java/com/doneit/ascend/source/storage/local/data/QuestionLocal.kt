package com.doneit.ascend.source.storage.local.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "questions")
data class QuestionLocal(
    @PrimaryKey
    @ColumnInfo(name = "question_id")
    var id: Long = Random.nextLong(),
    var title: String = "",
    var type: String = "",
    var createdAt: String = "",
    var updatedAt: String = "",

    @Ignore
    val options: List<AnswerOptionLocal> = listOf()
)