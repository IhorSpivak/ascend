package com.doneit.ascend.source.storage.local.data.first_time_login

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_items")
data class QuestionItemLocal(
    @PrimaryKey
    @ColumnInfo(name = "question_item_id")
    var id: Long = 0,
    var title: String = "",
    var type: String = "",
    var createdAt: String = "",
    var updatedAt: String = "",

    @ColumnInfo(name = "question_id")
    var questionId: Long = 0
)