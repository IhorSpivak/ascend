package com.doneit.ascend.domain.use_case.gateway

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.WebinarQuestionDTO
import com.doneit.ascend.domain.entity.webinar_question.QuestionSocketEntity
import com.doneit.ascend.domain.entity.webinar_question.WebinarQuestionEntity

interface IWebinarQuestionGateway {
    fun getQuestions(groupId: Long, request: WebinarQuestionDTO): LiveData<PagedList<WebinarQuestionEntity>>

    suspend fun create(groupId: Long, content: String): ResponseEntity<Unit, List<String>>

    suspend fun update(id: Long, content: String): ResponseEntity<Unit, List<String>>

    suspend fun delete(id: Long): ResponseEntity<Unit, List<String>>

    val questionStream: LiveData<QuestionSocketEntity>

    fun connectToChannel(groupId: Long)

    fun disconnect()

    fun insertQuestion(question: WebinarQuestionEntity)
    fun removeQuestionLocal(id: Long)

}