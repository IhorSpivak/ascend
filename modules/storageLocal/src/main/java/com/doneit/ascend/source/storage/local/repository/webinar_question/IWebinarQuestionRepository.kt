package com.doneit.ascend.source.storage.local.repository.webinar_question

import androidx.paging.DataSource
import com.doneit.ascend.source.storage.local.data.WebinarQuestionLocal

interface IWebinarQuestionRepository {
    suspend fun insert(question: WebinarQuestionLocal)
    suspend fun insert(questions: List<WebinarQuestionLocal>)
    suspend fun getAll(): DataSource.Factory<Int, WebinarQuestionLocal>
    suspend fun removeAllExcept(groupId: Long)
    suspend fun removeAll()
}