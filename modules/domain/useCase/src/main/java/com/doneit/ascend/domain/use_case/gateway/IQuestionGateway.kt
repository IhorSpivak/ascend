package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface IQuestionGateway {
    suspend fun getList(sessionToken: String): ResponseEntity<List<QuestionEntity>, List<String>>

    suspend fun getQuestionsList(): List<QuestionEntity>
    suspend fun insert(questions: List<QuestionEntity>)
    suspend fun deleteAllQuestions()
}