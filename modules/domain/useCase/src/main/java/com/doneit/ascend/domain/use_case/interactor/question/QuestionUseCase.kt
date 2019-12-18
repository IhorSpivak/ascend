package com.doneit.ascend.domain.use_case.interactor.question

import com.doneit.ascend.domain.entity.QuestionListEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface QuestionUseCase {
    suspend fun getList(sessionToken: String): ResponseEntity<QuestionListEntity, List<String>>
    suspend fun insert(questions: QuestionListEntity)
    suspend fun deleteAllQuestions()
    suspend fun getQuestionsList(): QuestionListEntity
}