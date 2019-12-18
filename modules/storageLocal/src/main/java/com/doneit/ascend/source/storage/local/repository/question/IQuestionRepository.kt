package com.doneit.ascend.source.storage.local.repository.question

import com.doneit.ascend.source.storage.local.data.QuestionListLocal
import com.doneit.ascend.source.storage.local.data.QuestionWithAnswerOptions

interface IQuestionRepository {

    suspend fun insert(questions: QuestionListLocal)
    suspend fun getAll(): QuestionWithAnswerOptions
    suspend fun removeAll()
}