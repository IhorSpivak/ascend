package com.doneit.ascend.domain.use_case.interactor.question

import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.domain.entity.common.RequestEntity

interface QuestionUseCase {
    suspend fun getList(sessionToken: String): RequestEntity<List<QuestionEntity>, List<String>>
}