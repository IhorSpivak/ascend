package com.doneit.ascend.source.storage.local.repository.question

import com.doneit.ascend.source.storage.local.data.QuestionLocal
import com.doneit.ascend.source.storage.local.data.QuestionWithAnswerOptions
import com.doneit.ascend.source.storage.local.repository.LocalDatabase

internal class QuestionRepository(
    private val database: LocalDatabase
) : IQuestionRepository {

    override suspend fun insert(questions: List<QuestionLocal>) {
        database.questionDao().insertAll(questions)
    }

    override suspend fun getAll(): List<QuestionWithAnswerOptions> {
        return database.questionDao().getAll()
    }

    override suspend fun removeAll() {
        database.questionDao().removeAll()
    }
}