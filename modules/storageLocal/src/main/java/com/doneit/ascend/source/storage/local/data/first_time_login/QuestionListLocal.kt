package com.doneit.ascend.source.storage.local.data.first_time_login

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.doneit.ascend.source.storage.local.data.CommunityLocal
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
) {
    init {
        id = hashCode().toLong()//to guarantee unique id
    }
}