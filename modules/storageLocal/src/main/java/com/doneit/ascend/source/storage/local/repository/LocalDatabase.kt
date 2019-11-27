package com.doneit.ascend.source.storage.local.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.doneit.ascend.source.storage.local.data.AnswerOptionLocal
import com.doneit.ascend.source.storage.local.data.QuestionLocal
import com.doneit.ascend.source.storage.local.repository.question.QuestionDao

@Database(
    entities = [QuestionLocal::class, AnswerOptionLocal::class],
    version = 1
)
internal abstract class LocalDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao

    companion object {
        const val NAME = "AscendDB"
    }
}