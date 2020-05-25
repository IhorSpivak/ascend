package com.doneit.ascend.source.storage.local.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "webinar_questions")
class WebinarQuestionLocal(
    @PrimaryKey val id: Long,
    val content: String,
    val createdAt: String?,
    val updatedAt: String?,
    val userId: Long,
    val fullName: String,
    @Embedded(prefix = "img") val image: ImageLocal?
)