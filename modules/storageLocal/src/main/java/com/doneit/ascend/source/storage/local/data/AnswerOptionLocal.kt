package com.doneit.ascend.source.storage.local.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(
    tableName = "answer_options"
)
data class AnswerOptionLocal(
    @PrimaryKey
    val id: Long = Random.nextLong(),
    val title: String = "",

    @ColumnInfo(name = "community_id")
    val communityId: Long = -1
)