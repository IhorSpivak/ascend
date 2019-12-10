package com.doneit.ascend.domain.use_case.interactor.question

import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface QuestionUseCase {
    suspend fun getList(sessionToken: String): ResponseEntity<List<QuestionEntity>, List<String>>
    suspend fun insert(questions: List<QuestionEntity>)
    suspend fun deleteAllQuestions()
    suspend fun getQuestionsList(): List<QuestionEntity>
}