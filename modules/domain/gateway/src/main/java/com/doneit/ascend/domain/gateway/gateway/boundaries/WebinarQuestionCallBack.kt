package com.doneit.ascend.domain.gateway.gateway.boundaries

import com.doneit.ascend.domain.entity.dto.WebinarQuestionDTO
import com.doneit.ascend.domain.entity.webinar_question.WebinarQuestionEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_entity.toEntity
import com.doneit.ascend.domain.gateway.common.mapper.to_locale.toLocal
import com.doneit.ascend.domain.gateway.common.mapper.to_remote.toRequest
import com.doneit.ascend.source.storage.local.repository.webinar_question.IWebinarQuestionRepository
import com.doneit.ascend.source.storage.remote.repository.group.webinar_questions.IWebinarQuestionsRepository
import kotlinx.coroutines.CoroutineScope

class WebinarQuestionCallBack(
    scope: CoroutineScope,
    private val local: IWebinarQuestionRepository,
    private val remote: IWebinarQuestionsRepository,
    private val model: WebinarQuestionDTO,
    private val groupId: Long
) : BaseBoundary<WebinarQuestionEntity>(scope) {


    override suspend fun fetchPage() {
        val response = remote.getQuestions(groupId, model.toRequest(pageIndexToLoad))
        if (response.isSuccessful) {
            val model = response.successModel?.questions?.map { it.toEntity() }
            model?.let {
                val loadedCount = model.size
                val remoteCount = response.successModel!!.count
                receivedItems(loadedCount, remoteCount)
                local.insert(model.map { it.toLocal() })
            }
        }
    }
}