package com.doneit.ascend.source.storage.local.repository

import androidx.room.Database
import androidx.room.RoomDatabase
import com.doneit.ascend.source.storage.local.data.AnswerOptionLocal
import com.doneit.ascend.source.storage.local.data.QuestionLocal
import com.doneit.ascend.source.storage.local.data.UserLocal
import com.doneit.ascend.source.storage.local.repository.question.QuestionDao
import com.doneit.ascend.source.storage.local.repository.user.UserDao

@Database(
    entities = [QuestionLocal::class, AnswerOptionLocal::class, UserLocal::class],
    version = 2
)
internal abstract class LocalDatabase : RoomDatabase() {

    abstract fun questionDao(): QuestionDao
    abstract fun userDao(): UserDao

    companion object {
        const val NAME = "AscendDB"
    }
}