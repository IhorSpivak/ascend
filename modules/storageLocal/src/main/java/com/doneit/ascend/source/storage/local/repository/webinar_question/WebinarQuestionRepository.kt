package com.doneit.ascend.source.storage.local.repository.webinar_question

import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.WebinarQuestionLocal

class WebinarQuestionRepository(
    private val dao: WebinarQuestionDao
) : IWebinarQuestionRepository {
    override suspend fun insert(question: WebinarQuestionLocal) {
        dao.insertQuestion(question)
    }

    override suspend fun insert(questions: List<WebinarQuestionLocal>) {
        dao.insertQuestions(questions)
    }

    override suspend fun getAll(): DataSource.Factory<Int, WebinarQuestionLocal> {
        return dao.getAll()
    }

    override suspend fun removeAllExcept(groupId: Long) {
        dao.removeAllExcept(groupId)
    }

    override suspend fun removeAll() {
        dao.removeAll()
    }

}