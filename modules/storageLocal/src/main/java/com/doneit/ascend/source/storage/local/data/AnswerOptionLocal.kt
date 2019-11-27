package com.doneit.ascend.source.storage.local.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(
    tableName = "answer_options",
    foreignKeys = [
        ForeignKey(
            entity = QuestionLocal::class,
            parentColumns = ["question_id"],
            childColumns = ["owner_id"]
        )
    ]
)
data class AnswerOptionLocal(
    @PrimaryKey
    val id: Long = Random.nextLong(),
    val title: String = "",
    val position: Long = -1,
    val createdAt: String = "",
    val updatedAt: String = "",
    @ColumnInfo(name = "owner_id")
    val questionId: Long = -1
)