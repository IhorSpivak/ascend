package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.QuestionListEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface IQuestionGateway {
    suspend fun getList(sessionToken: String): ResponseEntity<QuestionListEntity, List<String>>

    suspend fun getQuestionsList(): QuestionListEntity
    suspend fun insert(questions: QuestionListEntity)
    suspend fun deleteAllQuestions()
}