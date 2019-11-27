package com.doneit.ascend.source.storage.local.repository.question

import com.doneit.ascend.source.storage.local.data.QuestionLocal
import com.doneit.ascend.source.storage.local.data.QuestionWithAnswerOptions

interface IQuestionRepository {

    suspend fun insert(questions: List<QuestionLocal>)
    suspend fun getAll(): List<QuestionWithAnswerOptions>
    suspend fun removeAll()
}