package com.doneit.ascend.domain.use_case.gateway

import androidx.lifecycle.LiveData
import com.doneit.ascend.domain.entity.QuestionListEntity
import com.doneit.ascend.domain.entity.common.ResponseEntity

interface IQuestionGateway {
    suspend fun getList(): ResponseEntity<QuestionListEntity, List<String>>
    fun getQuestionsList(): LiveData<QuestionListEntity?>
}