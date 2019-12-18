package com.doneit.ascend.source.storage.local.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = "communities")
data class CommunityLocal(
    @PrimaryKey
    @ColumnInfo(name = "community_id")
    var id: Long = Random.nextLong(),
    var title: String = "",

    @ColumnInfo(name = "question_id")
    var questionId: Long = 0,

    @Ignore
    var options: List<AnswerOptionLocal> = listOf()
)