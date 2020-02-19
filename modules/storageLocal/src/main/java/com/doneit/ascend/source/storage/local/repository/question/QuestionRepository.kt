package com.doneit.ascend.source.storage.local.repository.question

import com.doneit.ascend.source.storage.local.data.first_time_login.QuestionListLocal
import com.doneit.ascend.source.storage.local.data.first_time_login.QuestionWithAnswerOptions
import com.doneit.ascend.source.storage.local.repository.LocalDatabase

internal class QuestionRepository(
    private val database: LocalDatabase
) : IQuestionRepository {

    override suspend fun insert(questions: QuestionListLocal) {
        database.questionDao().insertAll(questions)
    }

    override suspend fun getAll(): QuestionWithAnswerOptions? {
        return database.questionDao().getAllQuestions()
    }

    override suspend fun removeAll() {
        database.questionDao().removeAll()
    }
}