package com.doneit.ascend.source.storage.local.repository.question

import com.doneit.ascend.source.storage.local.data.first_time_login.QuestionListLocal
import com.doneit.ascend.source.storage.local.data.first_time_login.QuestionWithAnswerOptions

interface IQuestionRepository {

    suspend fun insert(questions: QuestionListLocal)
    suspend fun getAll(): QuestionWithAnswerOptions?
    suspend fun removeAll()
}