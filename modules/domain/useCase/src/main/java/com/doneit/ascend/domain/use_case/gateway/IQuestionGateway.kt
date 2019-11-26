package com.doneit.ascend.domain.use_case.gateway

import com.doneit.ascend.domain.entity.QuestionEntity
import com.doneit.ascend.domain.entity.common.RequestEntity

interface IQuestionGateway {
    suspend fun getList(sessionToken: String): RequestEntity<List<QuestionEntity>, List<String>>
}