package com.doneit.ascend.source.storage.local.data.first_time_login

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "answer_options"
)
data class AnswerOptionLocal(
    @PrimaryKey
    var id: Long = -1,
    val title: String = "",

    @ColumnInfo(name = "community_id")
    var communityId: Long = -1
) {
    init {
        id = hashCode().toLong()//to guarantee unique id
    }
}