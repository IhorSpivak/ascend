package com.doneit.ascend.source.storage.local.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlin.random.Random

//@Entity(
//    tableName = "questions_community", foreignKeys = (arrayOf(
//        ForeignKey(
//            entity = QuestionListLocal::class,
//            onUpdate = ForeignKey.CASCADE,
//            parentColumns = arrayOf("question_id"),
//            childColumns = arrayOf("question_community_id")
//        )
//    ))
//)
//data class QuestionCommunity(
//    @PrimaryKey
//    @ColumnInfo(name = "question_community_id")
//    var id: Long = Random.nextLong(),
//    @ColumnInfo(name = "question_id")
//    var questionId: Long? = 0,
//    @ColumnInfo(name = "community_id")
//    var communityId: Long? = 0
//)