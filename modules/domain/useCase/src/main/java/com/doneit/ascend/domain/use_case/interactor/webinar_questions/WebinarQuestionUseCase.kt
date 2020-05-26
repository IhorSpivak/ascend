package com.doneit.ascend.domain.use_case.interactor.webinar_questions

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.doneit.ascend.domain.entity.common.ResponseEntity
import com.doneit.ascend.domain.entity.dto.WebinarQuestionDTO
import com.doneit.ascend.domain.entity.webinar_question.QuestionSocketEntity
import com.doneit.ascend.domain.entity.webinar_question.WebinarQuestionEntity

interface WebinarQuestionUseCase {
    fun getWebinarQuestionLive(groupId: Long, request: WebinarQuestionDTO): LiveData<PagedList<WebinarQuestionEntity>>

    suspend fun createQuestion(groupId: Long, content: String): ResponseEntity<Unit, List<String>>
    suspend fun updateQuestion(id: Long, content: String): ResponseEntity<Unit, List<String>>
    suspend fun deleteQuestion(id: Long): ResponseEntity<Unit, List<String>>

    val questionStream: LiveData<QuestionSocketEntity?>
    fun connectToChannel(groupId: Long)
    fun disconnect()

    fun insertMessage(questionEntity: WebinarQuestionEntity)
}